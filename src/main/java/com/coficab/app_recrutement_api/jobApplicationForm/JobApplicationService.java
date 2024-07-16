package com.coficab.app_recrutement_api.jobApplicationForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JavaMailSender mailSender;

    public JobApplicationForm saveJobApplication(JobApplicationForm jobApplicationForm) {
        return jobApplicationRepository.save(jobApplicationForm);
    }

    public String saveFile(MultipartFile file, String type) throws IOException {
        if (file == null || file.isEmpty()) {
            return null; // Return null if file is not provided or is empty
        }

        if (!file.getContentType().equals("application/pdf")) {
            throw new IOException("Invalid file type. Only PDF files are allowed.");
        }

        String uploadDir = System.getProperty("user.dir") + "/uploads/" + type;
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        String filePath = uploadDir + "/" + fileName;
        file.transferTo(new File(filePath));
        return fileName; // Return only the file name
    }


    public void sendEmailNotification(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public List<JobApplicationFormResponse> findJobApplicationsForJobPost(Long jobPostId) {
        List<JobApplicationForm> jobApplications = jobApplicationRepository.findByJobPostId(jobPostId);

        return jobApplications.stream()
                .map(this::mapToResponse) // Assuming you have a method to map to response DTO
                .collect(Collectors.toList());
    }

    // Additional method to map JobApplicationForm to JobApplicationFormResponse
    private JobApplicationFormResponse mapToResponse(JobApplicationForm form) {
        // Implement mapping logic here as needed
        return new JobApplicationFormResponse(/* map fields */);
    }
}
