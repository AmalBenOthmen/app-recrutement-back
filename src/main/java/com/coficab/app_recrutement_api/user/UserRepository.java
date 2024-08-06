package com.coficab.app_recrutement_api.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository  extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Integer id);

    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = 'USER'")
    long countUsersByRoleUser();



}
