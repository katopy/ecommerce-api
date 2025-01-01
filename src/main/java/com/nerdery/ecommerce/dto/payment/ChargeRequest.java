package com.nerdery.ecommerce.dto.payment;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class ChargeRequest {

    private Long orderId;
    @Enumerated(EnumType.STRING)
    private Currency currency;

    public enum Currency{
        EUR, USD;
    }

}
