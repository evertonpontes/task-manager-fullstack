package com.example.taskmanager.app.repositories;

import com.example.taskmanager.app.entities.task.ScheduledWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduledWorkRepository extends JpaRepository<ScheduledWork, UUID> {
    @Query("SELECT sw.sortIndex FROM ScheduledWork sw WHERE sw.task.id = :taskId ORDER BY sw.sortIndex DESC LIMIT 1")
    Optional<BigDecimal> findTopByTaskIdSortBySortIndexDesc(@Param("taskId") UUID taskId);
}
