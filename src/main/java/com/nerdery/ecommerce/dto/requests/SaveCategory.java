package com.nerdery.ecommerce.dto.requests;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class SaveCategory {
    @NotBlank
    private final String name;
}
