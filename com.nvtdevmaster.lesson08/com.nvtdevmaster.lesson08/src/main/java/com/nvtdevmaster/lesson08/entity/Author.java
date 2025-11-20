package com.nvtdevmaster.lesson08.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;
    private String name;
    private String description;
    private String imgUrl;
    private String email;
    private String phone;
    private String address;
    private Boolean isActive;

    @ManyToMany(mappedBy = "authors")
    private List<Book> books = new ArrayList<>();
}
