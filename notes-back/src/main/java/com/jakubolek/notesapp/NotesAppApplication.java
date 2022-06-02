package com.jakubolek.notesapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class NotesAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotesAppApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* First run only (Adding users and roles to db) */
//    @Bean
//    CommandLineRunner run(UserService userService) {
//        return args -> {
//            userService.saveRole("ROLE_USER");
//            userService.saveRole("ROLE_ADMIN");
//
//            userService.saveUser( "testAdmin", "testAdmin@gmail.com", "testAdmin");
//            userService.saveUser( "test1", "test1@gmail.com", "test1");
//
//            userService.addRoleToUser("testAdmin", "ROLE_ADMIN");
//        };
//    }
}
