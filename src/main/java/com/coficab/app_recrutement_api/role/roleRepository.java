package com.coficab.app_recrutement_api.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface roleRepository  extends JpaRepository<Role , Integer> {

    Optional<Role> findByName(String role);
}
