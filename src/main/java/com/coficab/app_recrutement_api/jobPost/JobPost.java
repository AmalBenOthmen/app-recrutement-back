package com.coficab.app_recrutement_api.jobPost;

import com.coficab.app_recrutement_api.favoriteJobPostList.FavoriteJobPostList;
import com.coficab.app_recrutement_api.jobApplicationForm.JobApplicationForm;
import com.coficab.app_recrutement_api.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "job_post")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String responsibilities;

    @Column(nullable = false)
    private String requirementsANDskills;

    @Column(nullable = false)
    private LocalDateTime dateLine;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL)
    private List<JobApplicationForm> jobApplications;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;


    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteJobPostList> favoriteJobPostList;

}

