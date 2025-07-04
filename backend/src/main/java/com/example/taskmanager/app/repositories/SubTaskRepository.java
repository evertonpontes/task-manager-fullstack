package com.example.taskmanager.app.repositories;

import com.example.taskmanager.app.entities.task.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubTaskRepository extends JpaRepository<SubTask, UUID> {
}

