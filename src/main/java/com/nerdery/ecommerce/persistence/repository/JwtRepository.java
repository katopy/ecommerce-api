package com.nerdery.ecommerce.persistence.repository;

import com.nerdery.ecommerce.persistence.entity.TokenRevoked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtRepository extends JpaRepository<TokenRevoked, Long> {

    boolean existsByJti(String jti);

    boolean existsByJtiAndIsValidFalse(String jti);


    Optional<TokenRevoked> findAllByJti(String jwt);
}
