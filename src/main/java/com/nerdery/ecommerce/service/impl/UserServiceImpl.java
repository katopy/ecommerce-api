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

    private final PasswordEncoder passwordEncoder;

    @Override
    public User createOneCustomer(SaveUser newUser) {
        validatePassword(newUser);
        var user = User.builder()
                .username(newUser.getUsername())
                .password_hash(passwordEncoder.encode(newUser.getPassword()))
                .email(newUser.getEmail())
                .role(Role.ROLE_CUSTOMER)
                .build();
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public void validatePassword(SaveUser dto){
        if(!StringUtils.hasText(dto.getPassword()) ||
        !StringUtils.hasText(dto.getRepeatedPassword()) ||
        !dto.getPassword().equals(dto.getRepeatedPassword())){
            throw new InvalidPasswordException("Passwords don't match");
        }
    }
}
