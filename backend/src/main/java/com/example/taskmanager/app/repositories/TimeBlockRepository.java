package com.example.taskmanager.app.repositories;

import com.example.taskmanager.app.entities.task.TimeBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TimeBlockRepository extends JpaRepository<TimeBlock, UUID> {
}
