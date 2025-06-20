package com.progaming.tutorial.dto;

import lombok.Data;

@Data
public class RequestCreateUserDto {
    private String username;
    private String email;
    private String password;
}
