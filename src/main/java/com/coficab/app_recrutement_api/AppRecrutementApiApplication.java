package com.coficab.app_recrutement_api;

import com.coficab.app_recrutement_api.role.ERole;
import com.coficab.app_recrutement_api.role.Role;
import com.coficab.app_recrutement_api.role.roleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableJpaRepositories
public class AppRecrutementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppRecrutementApiApplication.class, args);
	}
	@Bean
	public CommandLineRunner runner(roleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName(ERole.USER.name()).isEmpty()) {
				roleRepository.save(Role.builder().name(ERole.USER.name()).build());
			}
			if (roleRepository.findByName(ERole.ADMIN.name()).isEmpty()) {
				roleRepository.save(Role.builder().name(ERole.ADMIN.name()).build());
			}
		};
	}
}
