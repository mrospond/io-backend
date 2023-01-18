package com.example.iobackend.database.repository;

import com.example.iobackend.database.entities.UserModel;
import com.example.iobackend.database.entities.VerificationToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUser(UserModel user);
}
