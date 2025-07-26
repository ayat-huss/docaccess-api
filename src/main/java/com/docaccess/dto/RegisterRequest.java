package com.docaccess.dto;


import com.docaccess.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    // Optional: allow setting roles during registration (e.g., ROLE_USER or ROLE_ADMIN)
    private Set<Role> roles;


}
