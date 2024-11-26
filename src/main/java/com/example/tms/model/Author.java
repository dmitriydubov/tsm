package com.example.tms.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "task_author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<Task> tasks = new HashSet<>();
}
