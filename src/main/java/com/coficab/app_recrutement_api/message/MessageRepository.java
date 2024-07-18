package com.coficab.app_recrutement_api.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<Message,Integer> {
    List<Message> findByIsReadFalse();
}
