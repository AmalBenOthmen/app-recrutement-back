package com.coficab.app_recrutement_api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile(Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email);
        UserProfileDTO userProfileDTO = new UserProfileDTO(
                user.getCreatedDate(),
                user.getLastModifiedDate(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail()
        );
        return ResponseEntity.ok(userProfileDTO);
    }}