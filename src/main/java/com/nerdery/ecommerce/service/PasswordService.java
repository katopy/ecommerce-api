package com.nerdery.ecommerce.service;

public interface PasswordService {
    void requestPasswordReset(String email);

    void resetPassword(String code, String newPassword);
}
