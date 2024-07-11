package com.coficab.app_recrutement_api.jobPost;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class JobPostResponse {

    private Integer id;
    private String title;
    private String description;
    private String responsibilities;
    private String requirementsANDskills;
    private LocalDateTime dateLine;
}
