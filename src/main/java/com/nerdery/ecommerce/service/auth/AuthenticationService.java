package com.nerdery.ecommerce.service.auth;


import com.nerdery.ecommerce.dto.RegisteredUser;
import com.nerdery.ecommerce.dto.SaveUser;
import com.nerdery.ecommerce.persistence.entity.Customer;
import com.nerdery.ecommerce.persistence.entity.User;
import com.nerdery.ecommerce.persistence.repository.CustomerRepository;
import com.nerdery.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final UserService userService;
    private final CustomerRepository customerRepository;

    private final JwtService jwtService;
    private AuthenticationManager authenticationManager;

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

}
