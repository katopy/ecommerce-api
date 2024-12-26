package com.nerdery.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredUser {
    private Long id;
    private String username;
    private String name;
    private String role;
    private String jwt;
}
