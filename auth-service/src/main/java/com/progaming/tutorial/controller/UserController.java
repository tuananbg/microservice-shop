package com.progaming.tutorial.controller;

import com.progaming.tutorial.dto.RequestCreateUserDto;
import com.progaming.tutorial.service.KeycloakAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private KeycloakAdminService keycloakAdminService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody RequestCreateUserDto request) {
        keycloakAdminService.createUserAndAssignRole(request);
        return ResponseEntity.ok("User created and role assigned!");
    }
}