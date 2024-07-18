package com.coficab.app_recrutement_api.favoriteJobApplicationFormList;

import com.coficab.app_recrutement_api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteJobApplicationFormListRepository extends JpaRepository<FavoriteJobApplicationFormList, Long> {
    List<FavoriteJobApplicationFormList> findAllByJobApplicationFormIdAndUserEmail(Long jobApplicationFormId, String userEmail);
    List<FavoriteJobApplicationFormList> findByUser(User user);

}
