package com.coficab.app_recrutement_api.user;

import com.coficab.app_recrutement_api.handler.MessageResponse;
import io.jsonwebtoken.io.IOException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByEmail(username).orElse(null);
        } else {
            return null;
        }
    }


    @GetMapping("/admin/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile(Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email);
        UserProfileDTO userProfileDTO = new UserProfileDTO(

                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword(),
                user.getPhoto()
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
                null,
                updatedUser.getPhoto()// Password should not be returned in the response
        );
        return ResponseEntity.ok(updatedProfileDTO);
    }


    @GetMapping("/admin/user-count")
    @PreAuthorize("hasRole('ADMIN')")
    public long getUserCount() {
        return userService.countUsersByRoleUser();
    }

    @PutMapping(value = "/ModifierPhoto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> ModifierUserPhoto(@RequestPart("file") MultipartFile file) throws IOException {
        User user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        if (fileName.contains("..")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Invalid file path"));
        }

        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/") || file.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Invalid file type or size"));
        }

        try {
            String photo = savePhoto(file);
            user.setPhoto(photo);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(photo));
        } catch (IOException | java.io.IOException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse("Could not upload the photo: " + file.getOriginalFilename() + "!"));
        }
    }

    private String savePhoto(MultipartFile file) throws IOException, java.io.IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path uploadPath = Paths.get("photos");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return  fileName;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/photo/{filename}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> getPhoto(@PathVariable String filename) throws IOException {
        try { Path filePath = Paths.get("photos").resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                return ResponseEntity.ok() .contentType(MediaType.parseMediaType(contentType)) .body(resource); }
            else { return ResponseEntity.notFound().build();
            } }
        catch (MalformedURLException e) { return ResponseEntity.badRequest().body(null); } catch (
                java.io.IOException e) {
            throw new RuntimeException(e);
        }
    } }