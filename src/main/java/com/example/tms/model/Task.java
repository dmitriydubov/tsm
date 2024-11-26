package com.example.tms.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Status status = Status.STATUS_PENDING;

    @Column(nullable = false)
    private Priority priority;

    @OneToMany(mappedBy = "task", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "author_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Author author;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "contractor_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Contractor contractor;

    @Column(nullable = false)
    private Date date;
}
