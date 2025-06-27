package com.example.taskmanager.app.repositories;

import com.example.taskmanager.app.entities.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    @Query("SELECT t.sortIndex FROM Task t WHERE t.project.id = :projectId ORDER BY t.sortIndex DESC LIMIT 1")
    Optional<BigDecimal> findTopByProjectIdSortBySortIndexDesc(@Param("projectId") UUID projectId);
}

