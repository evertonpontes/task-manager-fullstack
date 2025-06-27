package com.example.taskmanager.app.repositories;

import com.example.taskmanager.app.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findAllByUserId(UUID id);
    List<Project> findAllByFolderId(UUID id);
    @Query("SELECT p.sortIndex FROM Project p WHERE p.user.id = :userId ORDER BY p.sortIndex DESC LIMIT 1")
    Optional<BigDecimal> findTopByUserIdSortBySortIndexDesc(@Param("userId") UUID userId);
    @Query("SELECT p.sortIndex FROM Project p WHERE p.folder.id = :folderId ORDER BY p.sortIndex DESC LIMIT 1")
    Optional<BigDecimal> findTopByFolderIdSortBySortIndexDesc(@Param("folderId") UUID folderId);
}
