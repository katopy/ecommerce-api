package com.nerdery.ecommerce.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveUser {
    @Size(min = 4)
    private String name;
    @Size(min = 4)
    private String username;
    @Email
    private String email;
    @Size(min = 8)
    private String password;
    @Size(min = 8)
    private String repeatedPassword;
}
