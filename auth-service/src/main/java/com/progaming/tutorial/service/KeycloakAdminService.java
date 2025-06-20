package com.progaming.tutorial.service;

import com.progaming.tutorial.dto.RequestCreateUserDto;

public interface KeycloakAdminService {
    void createUserAndAssignRole(RequestCreateUserDto request);
}
