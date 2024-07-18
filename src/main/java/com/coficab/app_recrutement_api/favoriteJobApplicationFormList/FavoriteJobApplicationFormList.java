package com.coficab.app_recrutement_api.favoriteJobApplicationFormList;

import com.coficab.app_recrutement_api.jobApplicationForm.JobApplicationForm;
import com.coficab.app_recrutement_api.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "JobAppFormList")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteJobApplicationFormList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;

    @ManyToOne
    private JobApplicationForm jobApplicationForm;
}
