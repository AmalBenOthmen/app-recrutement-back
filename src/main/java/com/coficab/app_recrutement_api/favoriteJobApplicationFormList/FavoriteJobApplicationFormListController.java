package com.coficab.app_recrutement_api.favoriteJobApplicationFormList;

import com.coficab.app_recrutement_api.jobApplicationForm.JobApplicationForm;
import com.coficab.app_recrutement_api.jobApplicationForm.JobApplicationFormDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favorite-application-form-list")
@RequiredArgsConstructor
public class FavoriteJobApplicationFormListController {

    private final FavoriteJobApplicationFormListService favoriteJobApplicationFormListService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<String> addToWishlist(@RequestParam Long jobApplicationFormId,
                                                @RequestParam String userEmail) {
        favoriteJobApplicationFormListService.addToWishlist(jobApplicationFormId, userEmail);
        return ResponseEntity.ok("Job application form added to wishlist.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromWishlist(@RequestParam Long jobApplicationFormId,
                                                     @RequestParam String userEmail) {
        favoriteJobApplicationFormListService.removeFromWishlist(jobApplicationFormId, userEmail);
        return ResponseEntity.ok("Job application form removed from wishlist.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<List<FavoriteJobApplicationFormListDTO>> getWishlistEntries(@RequestParam String userEmail) {
        if (userEmail == null || userEmail.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<FavoriteJobApplicationFormList> wishlistEntries = favoriteJobApplicationFormListService.getWishlistEntries(userEmail);

        // Convert entities to DTOs
        List<FavoriteJobApplicationFormListDTO> dtoList = wishlistEntries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    private FavoriteJobApplicationFormListDTO convertToDTO(FavoriteJobApplicationFormList entity) {
        FavoriteJobApplicationFormListDTO dto = new FavoriteJobApplicationFormListDTO();
        dto.setId(entity.getId());
        dto.setJobApplicationForm(convertToDTO(entity.getJobApplicationForm()));
        return dto;
    }

    private JobApplicationFormDTO convertToDTO(JobApplicationForm entity) {
        JobApplicationFormDTO dto = new JobApplicationFormDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setGender(entity.getGender());
        dto.setAge(entity.getAge());
        dto.setAddress(entity.getAddress());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setEducationLevel(entity.getEducationLevel());
        dto.setSpeciality(entity.getSpeciality());
        dto.setLinkedinLink(entity.getLinkedinLink());
        dto.setCvPath(entity.getCvPath());
        dto.setAdditionalDocumentsPath(entity.getAdditionalDocumentsPath());
        dto.setCoverLetter(entity.getCoverLetter());
        if (entity.getJobPost() != null) {
            dto.setJobPostId(entity.getJobPost().getId());
        }
        return dto;
    }
}