package com.coficab.app_recrutement_api.jobPost;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, Long>, JpaSpecificationExecutor<JobPost> {
    @Query("""
            SELECT jobPost
            FROM JobPost jobPost
            """)
    List<JobPost> findAllJobPosts();

    @Query
            ("SELECT jp.title, COUNT(jp) FROM JobPost jp GROUP BY jp.title")
    List<Object[]> countJobPostsByTitle();

    List<JobPost> findAllByTitleContaining(String Title);

}

