package com.coficab.app_recrutement_api.jobPost;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPostDTO {
    private Long id;
    private String title;
    private String description;
    private String responsibilities;
    private String requirementsANDskills;
    private LocalDateTime dateLine;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
