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
                user.getCreatedDate(),
                user.getLastModifiedDate(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail()
        );
        return ResponseEntity.ok(userProfileDTO);
    }

    @PutMapping("/admin/editProfile")
    public ResponseEntity<UserProfileDTO> updateUserProfile(Principal principal, @RequestBody UserProfileDTO userProfileDTO) {
        String email = principal.getName();
        User updatedUser = userService.updateUser(email, userProfileDTO);
        UserProfileDTO updatedProfileDTO = new UserProfileDTO(
                updatedUser.getCreatedDate(),
                updatedUser.getLastModifiedDate(),
                updatedUser.getFirstname(),
                updatedUser.getLastname(),
                updatedUser.getEmail()
        );
        return ResponseEntity.ok(updatedProfileDTO);
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @PutMapping(value = "/admin/updatePhoto/{email}")
    public ResponseEntity<?> updateUserPhoto(@PathVariable("email") String email,
                                             @RequestParam("file") MultipartFile file) {

        // Find the user by email
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            String message = "User with email " + email + " not found!";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(message));
        }
        User user = optionalUser.get();

        // Handle file validation
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName.contains("..")) {
            String message = "Invalid file path!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
        }

        // Check file type
        if (!file.getContentType().startsWith("image/")) {
            String message = "File is not an image!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
        }

        // Check file size (e.g., limit to 5MB)
        if (file.getSize() > 5242880) {
            String message = "File size exceeds limit!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
        }

        try {
            // Convert the file to Base64
            user.setPhoto(Base64.getEncoder().encodeToString(file.getBytes()));
            userRepository.save(user);
            String message = "User photo updated successfully! " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
        } catch (IOException e) {
            String message = "Could not read the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
        } catch (Exception e) {
            String message = "Could not upload the photo: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(message));
        }
    }
    @GetMapping("/admin/user-count")
    @PreAuthorize("hasRole('ADMIN')")
    public long getUserCount() {
        return userService.countUsersByRoleUser();
    }


}