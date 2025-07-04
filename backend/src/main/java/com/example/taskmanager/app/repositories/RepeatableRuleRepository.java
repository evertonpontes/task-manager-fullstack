package com.example.taskmanager.app.repositories;

import com.example.taskmanager.app.entities.task.RepeatableRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RepeatableRuleRepository extends JpaRepository<RepeatableRule, UUID> {
}
