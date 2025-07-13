package com.example.taskmanager.app.repositories;

import com.example.taskmanager.app.entities.task.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubTaskRepository extends JpaRepository<SubTask, UUID> {
    @Query("SELECT MAX(t.rank) FROM SubTask t WHERE t.parentTask.id = :taskId")
    Optional<Long> findMaxRankByTaskId(@Param("taskId") UUID taskId);
}

