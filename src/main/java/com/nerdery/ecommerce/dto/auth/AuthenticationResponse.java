package com.nerdery.ecommerce.dto.auth;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String jwt;
    private String refreshToken;
}
