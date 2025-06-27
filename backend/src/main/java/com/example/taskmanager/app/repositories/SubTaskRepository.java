package com.example.taskmanager.app.repositories;

import com.example.taskmanager.app.entities.task.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubTaskRepository extends JpaRepository<SubTask, UUID> {
    @Query("SELECT st.sortIndex FROM SubTask st WHERE st.parentTask.id = :taskId ORDER BY st.sortIndex DESC LIMIT 1")
    Optional<BigDecimal> findTopByTaskIdSortBySortIndexDesc(@Param("taskId") UUID taskId);
}

