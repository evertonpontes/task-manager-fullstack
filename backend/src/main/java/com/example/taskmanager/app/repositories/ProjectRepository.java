package com.example.taskmanager.app.repositories;

import com.example.taskmanager.app.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findAllByUserId(UUID id);
    List<Project> findAllByFolderId(UUID id);
}
