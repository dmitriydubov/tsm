package com.example.tms.repository;

import com.example.tms.model.Comment;
import com.example.tms.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long> {

    @Modifying
    @Query(
            value = "DELETE FROM comment c USING tasks_comments tc " +
            "WHERE c.id = tc.comments_id AND tc.tasks_id = ?1 " +
            "AND NOT EXISTS (SELECT 1 FROM tasks_comments WHERE comments_id = c.id AND tasks_id != ?1)",
            nativeQuery = true
    )
    void deleteAllByTaskId(Long taskId);
}
