package com.coficab.app_recrutement_api.favoriteJobPostList;

import com.coficab.app_recrutement_api.jobApplicationForm.JobApplicationForm;
import com.coficab.app_recrutement_api.jobPost.JobPost;
import com.coficab.app_recrutement_api.jobPost.JobPostDTO;
import com.coficab.app_recrutement_api.jobPost.JobPostMapper;
import com.coficab.app_recrutement_api.jobPost.JobPostResponse;
import com.coficab.app_recrutement_api.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "favorite_job_post_list")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteJobPostList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_post_id")
    private JobPost jobPost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FavoriteStatus status;



}
