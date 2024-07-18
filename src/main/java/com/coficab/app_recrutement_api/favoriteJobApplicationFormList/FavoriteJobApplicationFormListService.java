package com.coficab.app_recrutement_api.favoriteJobApplicationFormList;


import com.coficab.app_recrutement_api.jobApplicationForm.JobApplicationForm;
import com.coficab.app_recrutement_api.jobApplicationForm.JobApplicationRepository;
import com.coficab.app_recrutement_api.user.User;
import com.coficab.app_recrutement_api.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteJobApplicationFormListService {

    private final FavoriteJobApplicationFormListRepository wishlistRepository;
    private final JobApplicationRepository jobApplicationFormRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addToWishlist(Long jobApplicationFormId, String userEmail) {
        try {
            JobApplicationForm jobApplicationForm = jobApplicationFormRepository.findById(jobApplicationFormId)
                    .orElseThrow(() -> new RuntimeException("Job application form not found for ID: " + jobApplicationFormId));

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found for email: " + userEmail));

            FavoriteJobApplicationFormList wishlist = new FavoriteJobApplicationFormList();
            wishlist.setJobApplicationForm(jobApplicationForm);
            wishlist.setUser(user);

            wishlistRepository.save(wishlist);
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            throw new RuntimeException("Error adding to wishlist: " + e.getMessage());
        }
    }

    @Transactional
    public void removeFromWishlist(Long jobApplicationFormId, String userEmail) {
        try {
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found for email: " + userEmail));

            // Fetch all wishlist entries for the given jobApplicationFormId and user
            List<FavoriteJobApplicationFormList> wishlistEntries = wishlistRepository.findAllByJobApplicationFormIdAndUserEmail(jobApplicationFormId, userEmail);

            if (!wishlistEntries.isEmpty()) {
                // Remove all found entries
                wishlistRepository.deleteAll(wishlistEntries);
            } else {
                throw new RuntimeException("Wishlist entry not found");
            }
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            throw new RuntimeException("Error removing from wishlist: " + e.getMessage());
        }
    }
    @Transactional
    public List<FavoriteJobApplicationFormList> getWishlistEntries(String userEmail) {
        try {
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found for email: " + userEmail));

            return wishlistRepository.findByUser(user);
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            throw new RuntimeException("Error fetching wishlist entries: " + e.getMessage());
        }
    }
}