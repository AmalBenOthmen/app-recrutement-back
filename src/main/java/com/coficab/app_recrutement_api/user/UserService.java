package com.coficab.app_recrutement_api.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;



@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User updateUser(String email, UserProfileDTO userProfileDTO) {
        User user = findByEmail(email);
        user.setFirstname(userProfileDTO.getFirstname());
        user.setLastname(userProfileDTO.getLastname());
        user.setEmail(userProfileDTO.getEmail());
        if (userProfileDTO.getPassword() != null && !userProfileDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userProfileDTO.getPassword()));
        }
        return userRepository.save(user);
    }
    public long countUsersByRoleUser() {
        return userRepository.countUsersByRoleUser();
    }

}