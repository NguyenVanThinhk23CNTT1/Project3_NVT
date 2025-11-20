package com.nvtdevmaster.lesson08.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;
    private String name;
    private String description;
    private String imgUrl;
    private Integer quantity;
    private Double price;
    private Boolean isActive;

    @ManyToMany
    @JoinTable(
            name = "Book_Author",
            joinColumns = @JoinColumn(name = "bookId"),
            inverseJoinColumns = @JoinColumn(name = "authorId")
    )
    private List<Author> authors = new ArrayList<>();
}
