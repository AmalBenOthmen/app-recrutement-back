package com.coficab.app_recrutement_api.jobApplicationForm;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class JobApplicationFormResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private String age;
    private String address;
    private String phoneNumber;
    private String email;
    private String educationLevel;
    private String speciality;
    private String linkedinLink;
    private String cvPath;
    private String additionalDocumentsPath;
    private String coverLetter;
    // Add other fields as needed
}
