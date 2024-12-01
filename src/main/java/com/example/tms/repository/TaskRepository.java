package com.example.tms.repository;

import com.example.tms.dto.ViewTaskResponse;
import com.example.tms.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByAssigneeId(long id, Pageable pageable);

    Page<Task> findByAuthorId(long id, Pageable pageable);
}
