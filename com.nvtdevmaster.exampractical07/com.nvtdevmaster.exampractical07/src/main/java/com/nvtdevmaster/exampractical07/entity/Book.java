package com.nvtdevmaster.exampractical07.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "books")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String imgUrl;
    Integer qty;
    Double price;
    Integer yearRelease;
    String author;
    Boolean status;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    Category category;
}
