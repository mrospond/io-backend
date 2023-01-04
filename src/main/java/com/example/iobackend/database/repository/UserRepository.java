package com.example.iobackend.database.repository;

import com.example.iobackend.database.entities.UserModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserModel, Long> {
    Optional<UserModel> findFirstByUsername(String username);
    boolean existsByUsername(String username);
}
