package com.coficab.app_recrutement_api.jobApplicationForm;

import com.coficab.app_recrutement_api.jobPost.JobPost;
import com.coficab.app_recrutement_api.jobPost.JobPostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/jobApplication")
public class JobApplicationController {

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private JobPostRepository jobPostRepository;

    @PostMapping("/apply")
    public ResponseEntity<?> submitForm(@RequestParam("firstName") String firstName,
                                        @RequestParam("lastName") String lastName,
                                        @RequestParam("gender") String gender,
                                        @RequestParam("age") String age,
                                        @RequestParam("address") String address,
                                        @RequestParam("phoneNumber") String phoneNumber,
                                        @RequestParam("email") String email,
                                        @RequestParam("educationLevel") String educationLevel,
                                        @RequestParam("speciality") String speciality,
                                        @RequestParam("linkedinLink") String linkedinLink,
                                        @RequestParam("cv") MultipartFile cv,
                                        @RequestParam(value = "additionalDocuments", required = false) MultipartFile additionalDocuments,
                                        @RequestParam("coverLetter") String coverLetter,
                                        @RequestParam("jobPostId") Long jobPostId,
                                        RedirectAttributes redirectAttributes) {

        // Retrieve the JobPost by jobPostId
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new EntityNotFoundException("Job post not found with ID: " + jobPostId));

        // Create a new instance of JobApplicationForm
        JobApplicationForm jobApplicationForm = new JobApplicationForm();

        // Set values from request parameters
        jobApplicationForm.setFirstName(firstName);
        jobApplicationForm.setLastName(lastName);
        jobApplicationForm.setGender(gender);
        jobApplicationForm.setAge(age);
        jobApplicationForm.setAddress(address);
        jobApplicationForm.setPhoneNumber(phoneNumber);
        jobApplicationForm.setEmail(email);
        jobApplicationForm.setEducationLevel(educationLevel);
        jobApplicationForm.setSpeciality(speciality);
        jobApplicationForm.setLinkedinLink(linkedinLink);
        jobApplicationForm.setCoverLetter(coverLetter);
        jobApplicationForm.setJobPost(jobPost); // Set the job post

        try {
            // Save CV file and get path
            String cvPath = jobApplicationService.saveFile(cv, "cv");
            jobApplicationForm.setCvPath(cvPath);

            // Save additional documents file if provided and get path
            if (additionalDocuments != null && !additionalDocuments.isEmpty()) {
                String additionalDocumentsPath = jobApplicationService.saveFile(additionalDocuments, "additionalDocuments");
                jobApplicationForm.setAdditionalDocumentsPath(additionalDocumentsPath);
            } else {
                jobApplicationForm.setAdditionalDocumentsPath(null); // Set to null if no additional documents provided
            }

            // Save job application
            jobApplicationService.saveJobApplication(jobApplicationForm);

            // Optionally, you can log or store the file paths somewhere if needed
            System.out.println("CV Path: " + cvPath);
            if (additionalDocuments != null && !additionalDocuments.isEmpty()) {
                System.out.println("Additional Documents Path: " + jobApplicationForm.getAdditionalDocumentsPath());
            }

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        }

        // Send email notification to user
        String userSubject = "Job Application Received";
        String userText = "Dear " + jobApplicationForm.getFirstName() + ",\n\nThank you for applying. We have received your application.";
        jobApplicationService.sendEmailNotification(jobApplicationForm.getEmail(), userSubject, userText);

        // Return a JSON response for success
        return ResponseEntity.ok().body("{\"message\": \"Form submitted successfully!\"}");
    }
    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping("/job-post/{jobPostId}")
    public ResponseEntity<List<JobApplicationFormResponse>> findJobApplicationsForJobPost(@PathVariable Long jobPostId) {
        List<JobApplicationFormResponse> jobApplications = jobApplicationService.findJobApplicationsForJobPost(jobPostId);
        return ResponseEntity.ok(jobApplications);
    }
}
