package com.nerdery.ecommerce.service.impl;

import com.nerdery.ecommerce.config.security.IAuthenticationFacade;
import com.nerdery.ecommerce.dto.user.SaveUser;
import com.nerdery.ecommerce.exception.InvalidPasswordException;
import com.nerdery.ecommerce.exception.ObjectNotFoundException;
import com.nerdery.ecommerce.persistence.entity.User;
import com.nerdery.ecommerce.persistence.entity.enums.Role;
import com.nerdery.ecommerce.persistence.repository.UserRepository;
import com.nerdery.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IAuthenticationFacade authenticationFacade;
    @Override
    public User createOneCustomer(SaveUser newUser) {
        validatePassword(newUser);
        var user = User.builder()
                .username(newUser.username())
                .password_hash(passwordEncoder.encode(newUser.password()))
                .email(newUser.email())
                .role(Role.ROLE_CUSTOMER)
                .build();
        return userRepository.save(user);
    }
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
    public void validatePassword(SaveUser dto){
        if(!StringUtils.hasText(dto.password()) ||
        !StringUtils.hasText(dto.repeatedPassword()) ||
        !dto.password().equals(dto.repeatedPassword())){
            throw new InvalidPasswordException("Passwords don't match");
        }
    }
    @Override
    public Long getAuthenticatedUserId() {
        Authentication authentication = authenticationFacade.getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userRepository.findUserByEmail(username)
                    .orElseThrow(() -> new IllegalStateException("User not found with username: " + username));
            return user.getUserId();
        }
        throw new IllegalStateException("User is not authenticated");
    }
    @Override
    public User findUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(()-> new ObjectNotFoundException("User not found with ID: " + userId ));
    }
}
