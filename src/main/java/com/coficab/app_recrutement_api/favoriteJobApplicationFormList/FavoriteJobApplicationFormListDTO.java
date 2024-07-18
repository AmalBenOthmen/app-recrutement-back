package com.coficab.app_recrutement_api.favoriteJobApplicationFormList;

import com.coficab.app_recrutement_api.jobApplicationForm.JobApplicationFormDTO;
import lombok.Data;

@Data
public class FavoriteJobApplicationFormListDTO {
    private Integer id;
    private JobApplicationFormDTO jobApplicationForm;
}
