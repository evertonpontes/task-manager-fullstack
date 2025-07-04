package com.example.taskmanager.app.repositories;

import com.example.taskmanager.app.entities.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, UUID> {
    @Query("SELECT MAX(ts.orderIndex) FROM TaskStatus ts WHERE ts.node.id = :nodeId")
    Optional<Long> findMaxOrderIndexByNodeId(@Param("nodeId") UUID nodeId);
}
