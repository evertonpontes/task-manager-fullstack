package com.example.taskmanager.app.repositories;

import com.example.taskmanager.app.entities.task.LoggedTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoggedTimeRepository extends JpaRepository<LoggedTime, UUID> {
    @Query("SELECT lt.sortIndex FROM LoggedTime lt WHERE lt.task.id = :taskId ORDER BY lt.sortIndex DESC LIMIT 1")
    Optional<BigDecimal> findTopByTaskIdSortBySortIndexDesc(@Param("taskId") UUID taskId);
}
