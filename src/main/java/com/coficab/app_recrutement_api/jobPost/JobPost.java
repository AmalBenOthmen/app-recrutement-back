package com.coficab.app_recrutement_api.jobPost;

import com.coficab.app_recrutement_api.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;





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
    private Integer id;

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

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;
}
