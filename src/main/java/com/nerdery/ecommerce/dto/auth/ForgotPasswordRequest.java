package com.nerdery.ecommerce.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ForgotPasswordRequest (
        @NotNull
        String oldPassword,
        @NotNull
        @Size(min = 8)
        String newPassword,
        @NotNull
        @Size(min = 8)
        String repeatedPassword

){
}
