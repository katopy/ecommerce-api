package com.nerdery.ecommerce.service.auth;


import com.nerdery.ecommerce.dto.responses.RegisteredUser;
import com.nerdery.ecommerce.dto.requests.SaveUser;
import com.nerdery.ecommerce.dto.auth.AuthenticationRequest;
import com.nerdery.ecommerce.dto.auth.AuthenticationResponse;
import com.nerdery.ecommerce.exception.ObjectNotFoundException;
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

    public RegisteredUser registerOneCustomer(SaveUser newUser) {
        User user = userService.createOneCustomer(newUser);

        Customer customer = new Customer();
        customer.setFirstName(newUser.getName());
        customer.setUserId(user);
        customerRepository.save(customer);

        RegisteredUser userDto = new RegisteredUser();
        userDto.setId(user.getUserId());
        userDto.setName(customer.getFirstName());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(newUser.getEmail());
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

        return extraClaims;
    }
    public AuthenticationResponse login(AuthenticationRequest authRequest){

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
        return userService.findByEmail(email).orElseThrow(()-> new ObjectNotFoundException("User not found. Email: " + email));
    }
}
