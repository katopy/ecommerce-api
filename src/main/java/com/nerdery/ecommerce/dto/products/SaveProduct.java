package com.nerdery.ecommerce.dto.products;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

public record SaveProduct(
        @NotBlank
        @NotNull
        String name,
        @DecimalMin(value = "0.01", message = "The price should be higher than 0")
        BigDecimal price,
        @Min(value = 1)
        Long categoryId,

        String details
) {
}
