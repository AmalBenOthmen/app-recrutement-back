package com.coficab.app_recrutement_api.user;

import com.coficab.app_recrutement_api.handler.MessageResponse;
import io.jsonwebtoken.io.IOException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile(Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email);
        UserProfileDTO userProfileDTO = new UserProfileDTO(

                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword()
        );
        return ResponseEntity.ok(userProfileDTO);
    }
    @PutMapping("/admin/editProfile")
    public ResponseEntity<UserProfileDTO> updateUserProfile(Principal principal, @RequestBody UserProfileDTO userProfileDTO) {
        String email = principal.getName();
        User updatedUser = userService.updateUser(email, userProfileDTO);
        UserProfileDTO updatedProfileDTO = new UserProfileDTO(
                updatedUser.getFirstname(),
                updatedUser.getLastname(),
                updatedUser.getEmail(),
                null // Password should not be returned in the response
        );
        return ResponseEntity.ok(updatedProfileDTO);
    }





    @GetMapping("/admin/user-count")
    @PreAuthorize("hasRole('ADMIN')")
    public long getUserCount() {
        return userService.countUsersByRoleUser();
    }


}