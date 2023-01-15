package com.example.iobackend.database.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "item_search_history")
public class ItemResultModel {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Column(length = 1024)
    private String url;
    private String price;
    @CreationTimestamp
    private LocalDateTime timestamp;
}
