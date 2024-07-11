package com.coficab.app_recrutement_api.jobPost;

import com.coficab.app_recrutement_api.jobPost.JobPost;
import com.coficab.app_recrutement_api.jobPost.JobPostRequest;
import com.coficab.app_recrutement_api.jobPost.JobPostResponse;
import com.coficab.app_recrutement_api.user.User;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class JobPostMapper {
    public JobPost toJobPost(JobPostRequest request, User user) {
        return JobPost.builder()
                .title(request.title())
                .description(request.description())
                .responsibilities(request.responsibilities())
                .requirementsANDskills(request.requirementsANDskills())
                .dateLine(request.dateLine())
                .createdBy(user)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }

    public JobPostResponse toJobPostResponse(JobPost jobPost) {
        return JobPostResponse.builder()
                .id(jobPost.getId())
                .title(jobPost.getTitle())
                .description(jobPost.getDescription())
                .responsibilities(jobPost.getResponsibilities())
                .requirementsANDskills(jobPost.getRequirementsANDskills())
                .dateLine(jobPost.getDateLine())
                .build();
    }
}
