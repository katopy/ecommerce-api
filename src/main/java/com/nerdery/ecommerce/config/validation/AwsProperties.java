package com.nerdery.ecommerce.config.validation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "aws.schema")
@Validated
public class AwsProperties {
    @NotBlank(message = "AWS Access key is required")
    private String accessKey;
    @NotBlank(message = "AWS Secret key is required")
    private String secretKey;
    @NotNull(message = "AWS access key is required")
    private String bucketName;
}
