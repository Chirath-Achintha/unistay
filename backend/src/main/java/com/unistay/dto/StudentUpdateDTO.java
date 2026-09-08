package com.unistay.dto;

/**
 * DTO for updating student information from the Admin Panel.
 * Only editable fields are included – id, email, role, and createdAt are not changeable here.
 */
public class StudentUpdateDTO {

    private String fullName;
    private String phone;
    private String university;
    private String gender;

    public StudentUpdateDTO() {}

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}
