package com.coficab.app_recrutement_api.favoriteJobPostList;

import com.coficab.app_recrutement_api.jobPost.JobPost;
import com.coficab.app_recrutement_api.jobPost.JobPostRepository;
import com.coficab.app_recrutement_api.user.User;
import com.coficab.app_recrutement_api.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteJobPostListService {
    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;
    private final FavoriteJobPostListRepository favoriteJobPostListRepository;

    @Transactional
    public void addToWishlist(Long jobPostId, String userEmail) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new RuntimeException("Job post not found for ID: " + jobPostId));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + userEmail));

        FavoriteJobPostList wishlist = new FavoriteJobPostList();
        wishlist.setJobPost(jobPost);
        wishlist.setUser(user);
        wishlist.setStatus(FavoriteStatus.FAVORITED);

        favoriteJobPostListRepository.save(wishlist);
    }

    @Transactional
    public void removeFromWishlist(Long jobPostId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + userEmail));

        // Fetch all wishlist entries for the given jobPostId and user
        List<FavoriteJobPostList> wishlistEntries = favoriteJobPostListRepository.findAllByJobPost_IdAndUser_Email(jobPostId, userEmail);

        if (!wishlistEntries.isEmpty()) {
            // Update the status to REMOVED instead of deleting
            wishlistEntries.forEach(entry -> entry.setStatus(FavoriteStatus.REMOVED));
            favoriteJobPostListRepository.saveAll(wishlistEntries);
        } else {
            throw new RuntimeException("Wishlist entry not found");
        }
    }

    @Transactional
    public List<FavoriteJobPostList> getWishlistEntries(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + userEmail));

        return favoriteJobPostListRepository.findByUser(user);
    }
}
