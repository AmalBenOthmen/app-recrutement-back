package com.coficab.app_recrutement_api.jobApplicationForm;

import com.coficab.app_recrutement_api.jobPost.JobPost;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "job_application")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String firstName;

    @NotEmpty
    @Column(nullable = false)
    private String lastName;

    @NotEmpty
    @Column(nullable = false)
    private String gender;

    @NotEmpty
    @Column(nullable = false)
    private String age;

    @NotEmpty
    @Column(nullable = false)
    private String address;

    @NotEmpty
    @Column(nullable = false)
    private String phoneNumber;

    @Email
    @NotEmpty
    @Column(nullable = false)
    private String email;

    @NotEmpty
    @Column(nullable = false)
    private String educationLevel;

    @NotEmpty
    @Column(nullable = false)
    private String speciality;

    @Column(nullable = false)
    private String linkedinLink;

    @NotEmpty
    @Column(nullable = false)
    private String cvPath;


    @Column(nullable = true)
    private String additionalDocumentsPath;

    @NotEmpty
    @Column(nullable = false)
    private String coverLetter;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_post_id", nullable = false)
    private JobPost jobPost;
}
