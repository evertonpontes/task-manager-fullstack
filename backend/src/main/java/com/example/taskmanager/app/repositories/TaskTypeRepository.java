package com.example.taskmanager.app.repositories;

import com.example.taskmanager.app.entities.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, UUID> {
    @Query("SELECT tt.sortIndex FROM TaskType tt WHERE tt.project.id = :projectId ORDER BY tt.sortIndex DESC LIMIT 1")
    Optional<BigDecimal> findTopByProjectIdSortBySortIndexDesc(@Param("projectId") UUID projectId);
}
