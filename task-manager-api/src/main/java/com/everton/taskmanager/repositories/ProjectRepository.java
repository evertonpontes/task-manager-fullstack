package com.everton.taskmanager.repositories;

import com.everton.taskmanager.entities.groups.projects.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, String> {
}
