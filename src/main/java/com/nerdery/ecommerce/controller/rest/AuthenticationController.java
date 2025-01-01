package com.nerdery.ecommerce.controller.rest;

import com.nerdery.ecommerce.dto.auth.AuthenticationRequest;
import com.nerdery.ecommerce.dto.auth.AuthenticationResponse;
import com.nerdery.ecommerce.dto.auth.ForgotPasswordRequest;
import com.nerdery.ecommerce.persistence.entity.User;
import com.nerdery.ecommerce.service.RevokedTokenService;
import com.nerdery.ecommerce.service.UserService;
import com.nerdery.ecommerce.service.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final RevokedTokenService blacklist;
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody @Valid AuthenticationRequest authenticationRequest) {
        AuthenticationResponse rsp = authenticationService.login(authenticationRequest);
        return ResponseEntity.ok(rsp);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public ResponseEntity<User> checkMyProfile(){
        User user = authenticationService.findLoggedInUser();
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest req){
        blacklist.addRevokeToken(req);
        return ResponseEntity.ok("Logged out successfully.");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ForgotPasswordRequest passwordRequest) {
        authenticationService.changePassword(passwordRequest);
        return ResponseEntity.ok("Password has been changed successfully.");
    }

}
