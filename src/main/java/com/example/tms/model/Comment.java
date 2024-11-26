package com.example.tms.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Date date;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "task_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Task task;
}
