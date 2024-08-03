package com.coficab.app_recrutement_api.favoriteJobPostList;

import com.coficab.app_recrutement_api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteJobPostListRepository extends JpaRepository<FavoriteJobPostList, Integer> {
        List<FavoriteJobPostList> findAllByJobPost_IdAndUser_Email(Long jobPostId, String userEmail);
        List<FavoriteJobPostList> findByUser(User user);
    }

