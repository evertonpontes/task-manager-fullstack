package com.example.taskmanager.app.repositories;

import com.example.taskmanager.app.entities.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, UUID> {
    @Query("SELECT ts.sortIndex FROM TaskStatus ts WHERE ts.project.id = :projectId ORDER BY ts.sortIndex DESC LIMIT 1")
    Optional<BigDecimal> findTopByProjectIdSortBySortIndexDesc(@Param("projectId") UUID projectId);
}
