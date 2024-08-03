package com.coficab.app_recrutement_api.jobPost;

import com.coficab.app_recrutement_api.jobApplicationForm.JobApplicationFormResponse;
import com.coficab.app_recrutement_api.user.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<Long> saveJobPost(
            @Valid @RequestBody JobPostRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.save(request, connectedUser));
    }

    @GetMapping("/{job-post-id}")
    public ResponseEntity<JobPostResponse> findJobPostById(
            @PathVariable("job-post-id") Long jobPostId
    ) {
        return ResponseEntity.ok(service.findById(jobPostId));
    }

    @GetMapping
    public ResponseEntity<List<JobPostResponse>> findAllJobPosts(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("User Roles: {}", user.getAuthorities());

        return ResponseEntity.ok(service.findAllJobPosts());
    }

    @GetMapping("/{job-post-id}/applications")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<JobApplicationFormResponse>> findJobApplicationsForJobPost(
            @PathVariable("job-post-id") Long jobPostId
    ) {
        return ResponseEntity.ok(service.findJobApplicationsForJobPost(jobPostId));
    }

    @GetMapping("/cv/{fileName:.+}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> downloadCV(@PathVariable String fileName, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("User Roles: {}", user.getAuthorities());
        Path filePath = Paths.get("C:\\Users\\lenovo\\Desktop\\app recrutement\\uploads\\cv").resolve(fileName).normalize();
        return downloadFile(filePath);
    }

    @GetMapping("/additionalDocuments/{fileName:.+}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> downloadAdditionalDocuments(@PathVariable String fileName, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("User Roles: {}", user.getAuthorities());
        Path filePath = Paths.get("C:\\Users\\lenovo\\Desktop\\app recrutement\\uploads\\additionalDocuments").resolve(fileName).normalize();
        return downloadFile(filePath);
    }

    private ResponseEntity<Resource> downloadFile(Path filePath) {
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/count-by-title")
    public List<Object[]> countJobPostsByTitle() {
        return service.countJobPostsByTitle();
    }

    @GetMapping("/jobPost/{Title}")
    public ResponseEntity<List<JobPostResponse>> getJobPostByTitle(@PathVariable String Title) {
        List<JobPostResponse> jobPostsList = service.getJobPostByTitle(Title);
        if (jobPostsList == null || jobPostsList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jobPostsList);
    }
    @PutMapping("/update/{job-post-id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobPostResponse> updateJobPost(
            @PathVariable("job-post-id") Long jobPostId,
            @Valid @RequestBody JobPostRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.update(jobPostId, request, connectedUser));
    }
    @DeleteMapping("/delete/{job-post-id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteJobPost(@PathVariable("job-post-id") Long jobPostId) {
        service.delete(jobPostId);
        return ResponseEntity.noContent().build();
    }


}