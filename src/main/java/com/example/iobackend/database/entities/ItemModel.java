package com.example.iobackend.database.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "items")
public class ItemModel {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private BigDecimal price;
    private String currency;
    private String service;
}
