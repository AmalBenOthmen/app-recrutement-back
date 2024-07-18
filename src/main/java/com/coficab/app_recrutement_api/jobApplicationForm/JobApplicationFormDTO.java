package com.coficab.app_recrutement_api.jobApplicationForm;

import com.coficab.app_recrutement_api.jobPost.JobPost;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationFormDTO {
    private Long id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String gender;

    @NotEmpty
    private String age;

    @NotEmpty
    private String address;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String phoneNumber;

    @NotEmpty
    private String educationLevel;

    @NotEmpty
    private String speciality;

    private String linkedinLink;

    @NotEmpty
    private String cvPath;

    private String additionalDocumentsPath;

    @NotEmpty
    private String coverLetter;

    private Long jobPostId;  // Assuming you want to expose jobPostId in DTO

    public JobApplicationFormDTO(JobApplicationForm jobApplicationForm) {
        this.id = jobApplicationForm.getId();
        this.firstName = jobApplicationForm.getFirstName();
        this.lastName = jobApplicationForm.getLastName();
        this.gender = jobApplicationForm.getGender();
        this.age = jobApplicationForm.getAge();
        this.address = jobApplicationForm.getAddress();
        this.email = jobApplicationForm.getEmail();
        this.phoneNumber = jobApplicationForm.getPhoneNumber();
        this.educationLevel = jobApplicationForm.getEducationLevel();
        this.speciality = jobApplicationForm.getSpeciality();
        this.linkedinLink = jobApplicationForm.getLinkedinLink();
        this.cvPath = jobApplicationForm.getCvPath();
        this.additionalDocumentsPath = jobApplicationForm.getAdditionalDocumentsPath();
        this.coverLetter = jobApplicationForm.getCoverLetter();
        if (jobApplicationForm.getJobPost() != null) {
            this.jobPostId = jobApplicationForm.getJobPost().getId();
        }
    }
}
