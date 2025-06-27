package com.example.taskmanager.app.repositories;

import com.example.taskmanager.app.entities.task.AttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttachedFileRepository extends JpaRepository<AttachedFile, UUID> {
    @Query("SELECT af.sortIndex FROM AttachedFile af WHERE af.task.id = :taskId ORDER BY af.sortIndex DESC LIMIT 1")
    Optional<BigDecimal> findTopByTaskIdSortBySortIndexDesc(@Param("taskId") UUID taskId);
}
