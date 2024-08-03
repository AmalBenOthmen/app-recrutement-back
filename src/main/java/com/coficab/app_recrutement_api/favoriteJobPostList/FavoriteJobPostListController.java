package com.coficab.app_recrutement_api.favoriteJobPostList;

import com.coficab.app_recrutement_api.jobPost.JobPost;
import com.coficab.app_recrutement_api.jobPost.JobPostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favorite-job-post-list/user")
@RequiredArgsConstructor
public class FavoriteJobPostListController {
    private final FavoriteJobPostListService favoriteJobPostListService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public ResponseEntity<String> addToWishlist(@RequestParam Long jobPostId, @RequestParam String userEmail) {
        favoriteJobPostListService.addToWishlist(jobPostId, userEmail);
        return ResponseEntity.ok("Job post added to wishlist.");
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromWishlist(@RequestParam Long jobPostId, @RequestParam String userEmail) {
        favoriteJobPostListService.removeFromWishlist(jobPostId, userEmail);
        return ResponseEntity.ok("Job post removed from wishlist.");
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/list")
    public ResponseEntity<List<FavoriteJobPostListDTO>> getWishlistEntries(@RequestParam String userEmail) {
        if (userEmail == null || userEmail.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<FavoriteJobPostList> wishlistEntries = favoriteJobPostListService.getWishlistEntries(userEmail);

        // Filter the list to include only FAVORITED job posts
        List<FavoriteJobPostList> favoritedEntries = wishlistEntries.stream()
                .filter(entry -> entry.getStatus() == FavoriteStatus.FAVORITED)
                .collect(Collectors.toList());

        // Convert entities to DTOs
        List<FavoriteJobPostListDTO> dtoList = favoritedEntries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    private FavoriteJobPostListDTO convertToDTO(FavoriteJobPostList entity) {
        FavoriteJobPostListDTO dto = new FavoriteJobPostListDTO();
        dto.setId(entity.getId());
        dto.setJobPost(convertToDTO(entity.getJobPost()));
        dto.setStatus(entity.getStatus());
        return dto;
    }

    private JobPostDTO convertToDTO(JobPost entity) {
        JobPostDTO dto = new JobPostDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setRequirementsANDskills(entity.getRequirementsANDskills());
        dto.setResponsibilities(entity.getResponsibilities());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        return dto;
    }
}