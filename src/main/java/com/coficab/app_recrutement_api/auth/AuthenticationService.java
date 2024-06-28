package com.coficab.app_recrutement_api.auth;

import com.coficab.app_recrutement_api.email.EmailService;
import com.coficab.app_recrutement_api.email.EmailTemplateName;
import com.coficab.app_recrutement_api.role.Role;
import com.coficab.app_recrutement_api.role.roleRepository;
import com.coficab.app_recrutement_api.security.JwtService;
import com.coficab.app_recrutement_api.user.Token;
import com.coficab.app_recrutement_api.user.TokenRepository;
import com.coficab.app_recrutement_api.user.User;
import com.coficab.app_recrutement_api.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final roleRepository RoleRepository;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    @Transactional
    public void register(RegistrationRequest request) throws MessagingException {
        initializeRolesIfNotExists(); // Ensure roles are initialized

        // Check if the email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already in use");
        }

        // Determine the role to assign
        String roleName = (request.getRole() != null && !request.getRole().isEmpty()) ? request.getRole() : "USER";
        var role = RoleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Role not found"));

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(new ArrayList<>()) // Initialize roles as mutable list
                .build();

        // Save the user first
        userRepository.save(user);
        // Assign the "USER" role after saving the user
        user.getRoles().add(role);
        userRepository.save(user);

        sendValidationEmail(user);
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("fullName", user.getFullName());

        var jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been sent to the same email address");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    private void initializeRolesIfNotExists() {
        if (RoleRepository.findByName("USER").isEmpty()) {
            RoleRepository.save(Role.builder().name("USER").build());
        }
        if (RoleRepository.findByName("ADMIN").isEmpty()) {
            RoleRepository.save(Role.builder().name("ADMIN").build());
        }
    }

    private String generateAndSaveActivationToken(User user) {
        // Generate a token
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

}
