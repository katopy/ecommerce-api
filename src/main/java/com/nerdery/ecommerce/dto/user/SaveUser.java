package com.nerdery.ecommerce.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record SaveUser(
        @Size(min = 4)
        String name,
        @Size(min = 4)
        String username,
        @Email
        String email,
        @Size(min = 8)
        String password,
        @Size(min = 8)
        String repeatedPassword
) {
}
