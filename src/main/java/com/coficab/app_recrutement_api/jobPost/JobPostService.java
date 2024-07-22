package com.coficab.app_recrutement_api.jobPost;

import com.coficab.app_recrutement_api.jobApplicationForm.JobApplicationForm;
import com.coficab.app_recrutement_api.jobApplicationForm.JobApplicationFormMapper;
import com.coficab.app_recrutement_api.jobApplicationForm.JobApplicationFormResponse;
import com.coficab.app_recrutement_api.jobApplicationForm.JobApplicationRepository;
import com.coficab.app_recrutement_api.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class JobPostService {
    private static final Logger logger = LoggerFactory.getLogger(JobPostService.class);

    private final JobPostRepository jobPostRepository;
    private final JobPostMapper jobPostMapper;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicationFormMapper jobApplicationFormMapper;

    public Long save(JobPostRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        JobPost jobPost = jobPostMapper.toJobPost(request, user);
        return jobPostRepository.save(jobPost).getId();
    }

    public JobPostResponse findById(Long jobPostId) {
        return jobPostRepository.findById(jobPostId)
                .map(jobPostMapper::toJobPostResponse)
                .orElseThrow(() -> new EntityNotFoundException("No job post found with ID:: " + jobPostId));
    }

    public List<JobPostResponse> findAllJobPosts() {
        return jobPostRepository.findAll().stream()
                .map(jobPostMapper::toJobPostResponse)
                .collect(Collectors.toList());
    }

    public List<JobApplicationFormResponse> findJobApplicationsForJobPost(Long jobPostId) {
        List<JobApplicationForm> jobApplications = jobApplicationRepository.findByJobPostId(jobPostId);

        return jobApplications.stream()
                .map(jobApplicationFormMapper::toJobApplicationFormResponse)
                .collect(Collectors.toList());
    }

    public List<Object[]> countJobPostsByTitle() {
        return jobPostRepository.countJobPostsByTitle();
    }



    public List<JobPostResponse> getJobPostByTitle(String title) {
        return jobPostRepository.findAllByTitleContaining(title)
                .stream()
                .map(jobPostMapper::toJobPostResponse)
                .collect(Collectors.toList());
    }
}