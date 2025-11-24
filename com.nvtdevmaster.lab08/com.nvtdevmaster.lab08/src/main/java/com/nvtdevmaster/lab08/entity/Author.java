package com.nvtdevmaster.lab08.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "author")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private String description;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<BookAuthor> bookAuthors = new ArrayList<>();
}
