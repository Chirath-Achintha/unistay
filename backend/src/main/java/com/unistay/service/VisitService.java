package com.unistay.service;

import com.unistay.dto.VisitRequestDTO;
import com.unistay.dto.VisitResponseDTO;
import com.unistay.dto.VisitStatusUpdateDTO;
import com.unistay.entity.Boarding;
import com.unistay.entity.User;
import com.unistay.entity.Visit;
import com.unistay.entity.VisitStatus;
import com.unistay.repository.BoardingRepository;
import com.unistay.repository.UserRepository;
import com.unistay.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final UserRepository userRepository;
    private final BoardingRepository boardingRepository;

    @Autowired
    public VisitService(VisitRepository visitRepository, UserRepository userRepository, BoardingRepository boardingRepository) {
        this.visitRepository = visitRepository;
        this.userRepository = userRepository;
        this.boardingRepository = boardingRepository;
    }

    @Transactional
    public VisitResponseDTO createVisitRequest(Long studentId, VisitRequestDTO dto) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found."));
        Boarding boarding = boardingRepository.findById(dto.getBoardingId())
                .orElseThrow(() -> new IllegalArgumentException("Boarding house not found."));

        LocalDate requestedDate = LocalDate.parse(dto.getRequestedDate());

        Visit visit = new Visit();
        visit.setStudent(student);
        visit.setBoarding(boarding);
        visit.setRequestedDate(requestedDate);
        visit.setStatus(VisitStatus.PENDING);

        Visit savedVisit = visitRepository.save(visit);

        // Build response manually to avoid lazy-load issues after save
        VisitResponseDTO response = new VisitResponseDTO();
        response.setId(savedVisit.getId());
        response.setStudentId(student.getId());
        response.setStudentName(student.getFullName());
        response.setStudentEmail(student.getEmail());
        response.setStudentPhone(student.getPhone());
        response.setBoardingId(boarding.getId());
        response.setBoardingName(boarding.getName());
        response.setRequestedDate(savedVisit.getRequestedDate());
        response.setProposedDate(savedVisit.getProposedDate());
        response.setStatus(savedVisit.getStatus());
        response.setCreatedAt(savedVisit.getCreatedAt());
        return response;
    }

    @Transactional(readOnly = true)
    public List<VisitResponseDTO> getVisitsForStudent(Long studentId) {
        return visitRepository.findByStudentIdOrderByCreatedAtDesc(studentId).stream()
                .map(VisitResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VisitResponseDTO> getVisitsForOwner(Long ownerId) {
        return visitRepository.findByBoardingOwnerIdOrderByCreatedAtDesc(ownerId).stream()
                .map(VisitResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public VisitResponseDTO updateVisitStatusByOwner(Long visitId, Long ownerId, VisitStatusUpdateDTO dto) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit request not found."));

        if (!visit.getBoarding().getOwner().getId().equals(ownerId)) {
            throw new IllegalArgumentException("You do not have permission to manage this visit request.");
        }

        visit.setStatus(dto.getStatus());
        if (dto.getStatus() == VisitStatus.ALTERNATIVE_DATE_PROPOSED) {
            if (dto.getProposedDate() == null || dto.getProposedDate().isBlank()) {
                throw new IllegalArgumentException("Proposed date must be provided.");
            }
            visit.setProposedDate(LocalDate.parse(dto.getProposedDate()));
        }

        Visit saved = visitRepository.save(visit);
        return new VisitResponseDTO(saved);
    }

    @Transactional
    public VisitResponseDTO acceptProposedDateByStudent(Long visitId, Long studentId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit request not found."));

        if (!visit.getStudent().getId().equals(studentId)) {
            throw new IllegalArgumentException("You do not have permission to manage this visit request.");
        }

        if (visit.getStatus() != VisitStatus.ALTERNATIVE_DATE_PROPOSED) {
            throw new IllegalArgumentException("There is no alternative date proposed for this visit.");
        }

        visit.setStatus(VisitStatus.ACCEPTED);
        visit.setRequestedDate(visit.getProposedDate());

        Visit saved = visitRepository.save(visit);
        return new VisitResponseDTO(saved);
    }
}
