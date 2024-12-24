package com.nerdery.ecommerce.service;

import com.nerdery.ecommerce.dto.SaveUser;
import com.nerdery.ecommerce.persistence.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    User createOneCustomer(SaveUser newUser);

    Optional<User> findOneByUsername(String name);
}
