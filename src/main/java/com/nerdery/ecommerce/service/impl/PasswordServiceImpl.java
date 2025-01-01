package com.nerdery.ecommerce.service.impl;

import com.nerdery.ecommerce.exception.ObjectNotFoundException;
import com.nerdery.ecommerce.persistence.entity.PasswordReset;
import com.nerdery.ecommerce.persistence.entity.User;
import com.nerdery.ecommerce.persistence.repository.PasswordResetRepository;
import com.nerdery.ecommerce.persistence.repository.UserRepository;
import com.nerdery.ecommerce.service.PasswordService;
import com.nerdery.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final UserService userService;
    private final PasswordResetRepository passwordResetRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void requestPasswordReset(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User not found with email: " + email));
        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setEmail(email);
        passwordReset.setCreatedAt(LocalDateTime.now());
        passwordReset.setExpiresAt(LocalDateTime.now().plusHours(1));
        passwordReset.setCode(UUID.randomUUID().toString());

        passwordResetRepository.save(passwordReset);
    }
    @Override
    public void resetPassword(String code, String newPassword) {
        PasswordReset passwordReset = passwordResetRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reset code."));
        if (passwordReset.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset code has expired.");
        }

        if (passwordReset.getUsedAt() != null) {
            throw new IllegalArgumentException("Reset code has already been used.");
        }

        User user = userService.findByEmail(passwordReset.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordReset.setUsedAt(LocalDateTime.now());
        passwordResetRepository.save(passwordReset);
    }
}
