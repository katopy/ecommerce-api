package com.nerdery.ecommerce.dto.auth;

public record ProfileResponse(
        String firstName,
        String lastName,

        String phoneNumber,
        String address

) {
}
