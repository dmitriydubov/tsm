package com.example.tms.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
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
    @Enumerated(EnumType.STRING)
    private Status status = Status.STATUS_PENDING;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "tasks_comments",
            joinColumns = { @JoinColumn(referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(referencedColumnName = "id") }
    )
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "user_admin_id")
    private User author;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "user_assignee_id")
    private User assignee;

    @Column(nullable = false)
    private Date date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
