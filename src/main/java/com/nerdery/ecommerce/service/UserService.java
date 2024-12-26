package com.nerdery.ecommerce.service;

import com.nerdery.ecommerce.dto.requests.SaveUser;
import com.nerdery.ecommerce.persistence.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    User createOneCustomer(SaveUser newUser);

    Optional<User> findByEmail(String email);
}
