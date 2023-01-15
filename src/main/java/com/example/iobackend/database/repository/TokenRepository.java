package com.example.iobackend.database.repository;

import com.example.iobackend.database.entities.VerificationToken;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<VerificationToken, Long> {
}
