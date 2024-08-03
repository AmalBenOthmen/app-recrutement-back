package com.coficab.app_recrutement_api.favoriteJobPostList;


import com.coficab.app_recrutement_api.jobPost.JobPostDTO;

import lombok.Data;


@Data
public class FavoriteJobPostListDTO {
    private Integer id;
    private JobPostDTO jobPost;
    private FavoriteStatus status;
}
