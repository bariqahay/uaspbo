package com.satuhati.satuhatis;

import com.satuhati.satuhatis.entity.User;
import com.satuhati.satuhatis.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SatuhatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SatuhatisApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.findByUsername("admin").ifPresentOrElse(
                user -> System.out.println("Admin already exists"),
                () -> {
                    User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .role("ADMIN")
                        .build();
                    userRepository.save(admin);
                    System.out.println("Default admin user created");
                }
            );
        };
    }
}
