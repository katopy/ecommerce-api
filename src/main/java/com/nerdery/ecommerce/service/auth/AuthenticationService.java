package com.nerdery.ecommerce.service.auth;


import com.nerdery.ecommerce.dto.auth.ForgotPasswordRequest;
import com.nerdery.ecommerce.dto.user.RegisteredUser;
import com.nerdery.ecommerce.dto.user.SaveUser;
import com.nerdery.ecommerce.dto.auth.AuthenticationRequest;
import com.nerdery.ecommerce.dto.auth.AuthenticationResponse;
import com.nerdery.ecommerce.exception.ObjectNotFoundException;
import com.nerdery.ecommerce.persistence.entity.Cart;
import com.nerdery.ecommerce.persistence.entity.Customer;
import com.nerdery.ecommerce.persistence.entity.User;
import com.nerdery.ecommerce.persistence.repository.CustomerRepository;
import com.nerdery.ecommerce.persistence.repository.UserRepository;
import com.nerdery.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final UserService userService;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public RegisteredUser registerOneCustomer(SaveUser newUser) {
        User user = userService.createOneCustomer(newUser);

        Customer customer = new Customer();
        customer.setFirstName(newUser.name());
        customer.setUserId(user);
        customerRepository.save(customer);

        Cart cart = new Cart();
        cart.setCustomer(customer);

        RegisteredUser userDto = new RegisteredUser();
        userDto.setId(user.getUserId());
        userDto.setName(customer.getFirstName());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(newUser.email());
        userDto.setRole(user.getRole().name());

        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        userDto.setJwt(jwt);
        return userDto;
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getUsername());
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("authorities", user.getAuthorities());
        extraClaims.put("userId", user.getUserId());
        System.out.println("USER ID IN AUTH: " + user.getUserId());
        return extraClaims;
    }

    public AuthenticationResponse login(AuthenticationRequest authRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()));
        var user = userRepository.findUserByEmail(
                authRequest.getEmail()
        ).orElseThrow();

        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setJwt(jwt);
        return authResponse;
    }

    public User findLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        return userService.findByEmail(email).orElseThrow(() -> new ObjectNotFoundException("User not found. Email: " + email));
    }

    public void changePassword(ForgotPasswordRequest request) {
        User user = findLoggedInUser();
        if(!passwordEncoder.matches(request.oldPassword(), user.getPasswordHash())){
            throw new IllegalArgumentException("Old password is incorrect.");
        }
        if (!request.newPassword().equals(request.repeatedPassword())) {
            throw new IllegalArgumentException("New password and repeated password do not match.");
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

}
