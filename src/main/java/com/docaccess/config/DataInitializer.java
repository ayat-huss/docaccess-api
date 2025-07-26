package com.docaccess.config;


import com.docaccess.entity.User;
import com.docaccess.enums.Role;
import com.docaccess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.EnumSet;
import java.util.Set;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.findByUsername("admin").isPresent()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles((EnumSet.of(Role.ADMIN)));

            User user = new User();
            user.setUsername("user1");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRoles((EnumSet.of(Role.USER)));

            userRepository.save(admin);
            userRepository.save(user);
        }
    }
}
