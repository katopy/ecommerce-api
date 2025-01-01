package com.nerdery.ecommerce.config.validation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "security.schema")
@Validated
public class SecurityProperties {

    @NotBlank(message = "JWT Secret Key is required")
    private String secretKey;
    @Min(value = 1, message = "JWT Expiration time must be greater than 0 minutes")
    private long expirationInMinutes;

}
