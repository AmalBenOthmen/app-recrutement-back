package com.coficab.app_recrutement_api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {




    private final JwtFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(
                                        "/auth/**",
                                        "/jobApplication/apply",
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html",

                                        "/files/**"
                                ).permitAll()
                                .requestMatchers(HttpMethod.POST, "/job-posts").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/job-posts/{job-post-id}").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/job-posts").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/jobApplication/jobPost/{jobPostId}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/job-posts/{job-post-id}/applications").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/job-posts/cv/{fileName:.+}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/job-posts/additionalDocuments/{fileName:.+}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/favorite-application-form-list/add").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/favorite-application-form-list/remove").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/favorite-application-form-list/list").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/messages/send").hasRole("USER")
                                .requestMatchers(HttpMethod.GET, "/messages/unread").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/messages/{id}/read").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/messages/all").hasRole("ADMIN")

                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
