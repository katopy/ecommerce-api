package com.nerdery.ecommerce.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private String id;
    private Long amount;
    private String currency;
    private String clientSecret;
}
