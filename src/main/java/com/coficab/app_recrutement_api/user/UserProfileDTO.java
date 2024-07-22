package com.coficab.app_recrutement_api.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserProfileDTO {

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    public UserProfileDTO(LocalDateTime createdDate, LocalDateTime lastModifiedDate, String firstname, String lastname, String email) {
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }
    public UserProfileDTO() {}

}
