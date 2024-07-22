package com.coficab.app_recrutement_api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
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
}
