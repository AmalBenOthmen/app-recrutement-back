package com.coficab.app_recrutement_api.jobPost;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;




public record JobPostRequest(
        Long id,
        @NotNull(message = "Title is required")
        @NotEmpty(message = "Title is required")
        String title,
        @NotNull(message = "Description is required")
        @NotEmpty(message = "Description is required")
        String description,
        @NotNull(message = "Responsibilities are required")
        @NotEmpty(message = "Responsibilities are required")
        String responsibilities,
        @NotNull(message = "Requirements and skills are required")
        @NotEmpty(message = "Requirements and skills are required")
        String requirementsANDskills,
        LocalDateTime dateLine
) {
}


