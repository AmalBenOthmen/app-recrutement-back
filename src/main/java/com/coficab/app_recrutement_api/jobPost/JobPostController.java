package com.coficab.app_recrutement_api.jobPost;

import com.coficab.app_recrutement_api.user.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/job-posts")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Tag(name = "JobPost")
public class JobPostController {
    private static final Logger log = LoggerFactory.getLogger(JobPostController.class);
    private final JobPostService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> saveJobPost(
            @Valid @RequestBody JobPostRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.save(request, connectedUser));
    }

    @GetMapping("/{job-post-id}")
    public ResponseEntity<JobPostResponse> findJobPostById(
            @PathVariable("job-post-id") Integer jobPostId
    ) {
        return ResponseEntity.ok(service.findById(jobPostId));
    }

    @GetMapping
    public ResponseEntity<List<JobPostResponse>> findAllJobPosts(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("User Roles: {}", user.getAuthorities());

        // No need to log the token here as it's handled by the filter
        return ResponseEntity.ok(service.findAllJobPosts());
    }
}
