package com.example.iobackend.database.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserModel {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    private String passwordHash;
}
