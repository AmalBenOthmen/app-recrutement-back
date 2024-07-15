package com.coficab.app_recrutement_api.jobApplicationForm;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplicationForm, Long> {
    List<JobApplicationForm> findByJobPostId(Long jobPostId);

}
