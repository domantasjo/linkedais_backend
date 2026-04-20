package com.linkedais.backend.service;

import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Ši klasė pasileidžia automatiškai kaskart, kai įjungi Spring Boot serverį.
 * Ji puikiai tinka pradinių duomenų (seed data) sukūrimui.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Patikriname, ar adminas jau egzistuoja duomenų bazėje
        if (userRepository.findByEmail("admin@ktu.lt").isEmpty()) {

            // Sukuriame naują vartotoją
            User admin = new User();
            admin.setName("Pagrindinis Administratorius");
            admin.setEmail("admin@ktu.lt");
            // Slaptažodį BŪTINA užkoduoti!
            admin.setPassword(passwordEncoder.encode("admin123"));

            // suteikiame admin role
            admin.setRole("ADMIN");

            // Išsaugome į duomenų bazę
            userRepository.save(admin);

            System.out.println("========================================================");
            System.out.println("SĖKMĖ: Sukurtas testinis adminas!");
            System.out.println("El. paštas: admin@ktu.lt");
            System.out.println("Slaptažodis: admin123");
            System.out.println("========================================================");
        }
    }
}