package com.nerdery.ecommerce.config.validation;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ConfigurationProperties(prefix = "db.schema")
@Validated
public class DBProperties {
    @NotBlank(message = "Database URL is required")
    private String dbUrl;
    @NotBlank(message = "Database password is required")
    private String dbPassword;
    @NotNull(message = "Database username is required")
    private String dbUsername;
    @Size(min = 0, message = "Database schema should be at least 0 characters long")
    private String dbSchema;
}
