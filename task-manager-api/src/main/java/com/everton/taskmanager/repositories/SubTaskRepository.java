package com.everton.taskmanager.repositories;

import com.everton.taskmanager.entities.tasks.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubTaskRepository extends JpaRepository<SubTask, String> {
    @Query("SELECT COALESCE(MAX(s.sortIndex), 0) FROM SubTask s WHERE s.task.id = :taskId")
    public Double findMaxSortIndexByTask(@Param("taskId") String taskId);
}
