package com.nerdery.ecommerce.controller.rest;

import com.nerdery.ecommerce.dto.auth.AuthenticationRequest;
import com.nerdery.ecommerce.dto.auth.AuthenticationResponse;
import com.nerdery.ecommerce.persistence.entity.User;
import com.nerdery.ecommerce.service.auth.AuthenticationService;
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

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody @Valid AuthenticationRequest authenticationRequest) {

        AuthenticationResponse rsp = authenticationService.login(authenticationRequest);
        return ResponseEntity.ok(rsp);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR', 'ROLE_ASSISTANT_ADMINISTRATOR', 'ROLE_CUSTOMER')")
    @GetMapping("/profile")
    public ResponseEntity<User> checkMyProfile(){
        User user = authenticationService.findLoggedInUser();
        return ResponseEntity.ok(user);

    }

}
