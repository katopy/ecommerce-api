package com.nerdery.ecommerce.config.validation;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "stripe.schema")
@Validated
public class StripeProperties {
    @NotBlank(message = "Stripe API Key is required")
    private String key;

    @NotBlank(message = "Stripe Webhook Secret is required")
    private String webhookSecret;


}
