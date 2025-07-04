package com.example.taskmanager.app.repositories;

import com.example.taskmanager.app.entities.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, UUID> {
    @Query("SELECT MAX(tt.orderIndex) FROM TaskType tt WHERE tt.node.id = :nodeId")
    Optional<Long> findMaxOrderIndexByNodeId(@Param("nodeId") UUID nodeId);
}
