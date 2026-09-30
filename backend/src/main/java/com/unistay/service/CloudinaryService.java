package com.unistay.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service to handle image uploads to Cloudinary.
 */
@Service
public class CloudinaryService {

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/webp", "image/gif", "image/jpg"
    );

    private static final long MAX_SIZE_BYTES = 10 * 1024 * 1024; // 10 MB

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Validates and uploads a single image file to Cloudinary.
     * @param file  The multipart file from the HTTP request.
     * @return      The secure Cloudinary URL string.
     */
    @SuppressWarnings("unchecked")
    public String uploadImage(MultipartFile file) {
        validateImage(file);

        try {
            if (cloudinary != null && cloudinary.config != null && cloudinary.config.cloudName != null && !cloudinary.config.cloudName.isEmpty()) {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.asMap(
                                "folder", "unistay/boardings",
                                "resource_type", "image",
                                "use_filename", false,
                                "unique_filename", true
                        )
                );
                String url = (String) uploadResult.get("secure_url");
                if (url != null && !url.isEmpty()) {
                    return url;
                }
            }
        } catch (Exception e) {
            System.err.println("Cloudinary upload failed, falling back to local file storage: " + e.getMessage());
        }

        return saveLocally(file);
    }

    private String saveLocally(MultipartFile file) {
        try {
            java.nio.file.Path uploadPath = java.nio.file.Paths.get("uploads");
            if (!java.nio.file.Files.exists(uploadPath)) {
                java.nio.file.Files.createDirectories(uploadPath);
            }
            String ext = ".jpg";
            String originalName = file.getOriginalFilename();
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }
            String filename = java.util.UUID.randomUUID().toString() + ext;
            java.nio.file.Path filePath = uploadPath.resolve(filename);
            java.nio.file.Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + filename;
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to save image locally: " + e.getMessage(), e);
        }
    }

    /**
     * Validates that the file is a non-empty image within the size limit.
     */
    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file cannot be empty.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException(
                    "Invalid file type '" + contentType + "'. Only JPEG, PNG, WEBP and GIF images are allowed."
            );
        }

        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new IllegalArgumentException(
                    "File '" + file.getOriginalFilename() + "' exceeds the 10MB size limit."
            );
        }
    }
}
