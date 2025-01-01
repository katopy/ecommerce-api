package com.nerdery.ecommerce.controller.rest;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nerdery.ecommerce.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/password-reset")
public class PasswordController {

    private final PasswordService passwordResetService;

    @PostMapping("/request")
    @RateLimiter(name = "resetPasswordRequest")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email){
        passwordResetService.requestPasswordReset(email);
        return ResponseEntity.ok("Password reset code has been sent");
    }

    @PostMapping("/reset")
    @RateLimiter(name = "resetPasswordRequest")
    public ResponseEntity<String> resetPassword(
            @RequestParam String code,
            @RequestParam String newPassword) {
        passwordResetService.resetPassword(code, newPassword);
        return ResponseEntity.ok("Password has been reset successfully.");
    }


}
