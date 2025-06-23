package com.example.taskmanager.app.repositories;

import com.example.taskmanager.app.entities.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FolderRepository extends JpaRepository<Folder, UUID> {
    List<Folder> findAllByUserId(UUID id);
    
    @Query("SELECT f.sortIndex FROM Folder f WHERE f.user.id = :userId ORDER BY f.sortIndex DESC LIMIT 1")
    Optional<BigDecimal> findTopByUserIdSortBySortIndexDesc(@Param("userId") UUID userId);
}
