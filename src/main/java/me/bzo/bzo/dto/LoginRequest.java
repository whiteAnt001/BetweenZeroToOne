package me.bzo.bzo.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String name;
    private String email;
    private String password;
    private String role;
}
