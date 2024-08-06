package com.coficab.app_recrutement_api.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserProfileDTO {


    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String photo;


    public UserProfileDTO( String firstname, String lastname, String email,String password, String photo) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password=password;
        this.photo=photo;
    }
    public UserProfileDTO() {}

}
