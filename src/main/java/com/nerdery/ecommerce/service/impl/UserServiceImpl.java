package com.nerdery.ecommerce.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.nerdery.ecommerce.dto.SaveUser;
import com.nerdery.ecommerce.exception.InvalidPasswordException;
import com.nerdery.ecommerce.persistence.entity.Customer;
import com.nerdery.ecommerce.persistence.entity.User;
import com.nerdery.ecommerce.persistence.entity.enums.Role;
import com.nerdery.ecommerce.persistence.repository.CustomerRepository;
import com.nerdery.ecommerce.persistence.repository.UserRepository;
import com.nerdery.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User createOneCustomer(SaveUser newUser) {
        validatePassword(newUser);

        User user = new User();
        user.setUsername(newUser.getUsername());
        user.setPassword_hash(passwordEncoder.encode(newUser.getPassword()));
        user.setRole(Role.ROLE_CUSTOMER);

        user = userRepository.save(user);

        Customer customer = new Customer();
        customer.setFirstName(newUser.getName());
        customer.setUserId(user);
        customerRepository.save(customer);
        return user;
    }

    @Override
    public Optional<User> findOneByUsername(String name) {
        return Optional.empty();
    }

    public void validatePassword(SaveUser dto){
        if(!StringUtils.hasText(dto.getPassword()) ||
        !StringUtils.hasText(dto.getRepeatedPassword()) ||
        !dto.getPassword().equals(dto.getRepeatedPassword())){
            throw new InvalidPasswordException("Passwords don't match");
        }
    }
}
