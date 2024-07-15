package com.coficab.app_recrutement_api.jobApplicationForm;

import org.springframework.stereotype.Component;

@Component
public class JobApplicationFormMapper {

    public JobApplicationFormResponse toJobApplicationFormResponse(JobApplicationForm jobApplicationForm) {
        JobApplicationFormResponse response = new JobApplicationFormResponse();
        response.setId(jobApplicationForm.getId());
        response.setFirstName(jobApplicationForm.getFirstName());
        response.setLastName(jobApplicationForm.getLastName());
        response.setGender(jobApplicationForm.getGender());
        response.setAge(jobApplicationForm.getAge());
        response.setAddress(jobApplicationForm.getAddress());
        response.setPhoneNumber(jobApplicationForm.getPhoneNumber());
        response.setEmail(jobApplicationForm.getEmail());
        response.setEducationLevel(jobApplicationForm.getEducationLevel());
        response.setSpeciality(jobApplicationForm.getSpeciality());
        response.setLinkedinLink(jobApplicationForm.getLinkedinLink());
        response.setCvPath(jobApplicationForm.getCvPath());
        response.setAdditionalDocumentsPath(jobApplicationForm.getAdditionalDocumentsPath());
        response.setCoverLetter(jobApplicationForm.getCoverLetter());
        // Set other fields as needed

        return response;
    }
}
