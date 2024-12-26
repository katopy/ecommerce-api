package com.nerdery.ecommerce.dto.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveProduct {

    @NotBlank
    @NotNull
    private String name;
    @DecimalMin(value = "0.01", message = "The price should be higher than 0")
    private BigDecimal price;
    @Min(value = 1)
    private Long categoryId;

}
