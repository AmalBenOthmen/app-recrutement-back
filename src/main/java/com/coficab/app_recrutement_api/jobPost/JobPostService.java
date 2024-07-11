package com.coficab.app_recrutement_api.jobPost;

import com.coficab.app_recrutement_api.user.User;
import com.coficab.app_recrutement_api.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class JobPostService {

    private final JobPostRepository jobPostRepository;
    private final JobPostMapper jobPostMapper;

    public Integer save(JobPostRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        JobPost jobPost = jobPostMapper.toJobPost(request, user);
        return jobPostRepository.save(jobPost).getId();
    }

    public JobPostResponse findById(Integer jobPostId) {
        return jobPostRepository.findById(jobPostId)
                .map(jobPostMapper::toJobPostResponse)
                .orElseThrow(() -> new EntityNotFoundException("No job post found with ID:: " + jobPostId));
    }

    public List<JobPostResponse> findAllJobPosts() {
        return jobPostRepository.findAll().stream()
                .map(jobPostMapper::toJobPostResponse)
                .toList();
    }
}
