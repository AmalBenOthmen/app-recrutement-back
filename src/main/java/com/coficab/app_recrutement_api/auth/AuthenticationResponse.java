package com.coficab.app_recrutement_api.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    public  String token;
    public String role;
}
