package com.tastytown;

import java.io.File;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tastytown.constants.Role;
import com.tastytown.entity.UserEntity;
import com.tastytown.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class TastyTownApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${upload.file.dir}")
    private String FILE_DIR;

    public static void main(String[] args) {
        SpringApplication.run(TastyTownApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {                
        Optional<UserEntity> existingAdmin = userRepository.findByUserEmail("admin@gmail.com");

        if (existingAdmin.isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setUserEmail("admin@gmail.com");
            admin.setUserPassword(passwordEncoder.encode("admin"));
            admin.setRole(Role.ROLE_ADMIN); //
            userRepository.save(admin);
            System.out.println("Default admin created: admin@gmail.com / admin");
        } else {
            System.out.println(" Default admin already exists.");
        }


        // to create the image folder
        var file = new File(FILE_DIR);
        if (!file.exists()) {
            file.mkdir();
            System.out.println("folder created to store images");
        }

        System.out.println("Folder already exists with name " + FILE_DIR);
    }
}
