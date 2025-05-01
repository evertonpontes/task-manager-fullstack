package com.everton.taskmanager.repositories;

import com.everton.taskmanager.entities.tasks.tasktiming.LoggedTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoggedTimeRepository extends JpaRepository<LoggedTime, String> {
    @Query("SELECT COALESCE(MAX(l.sortIndex), 0) FROM LoggedTime l WHERE l.task.id = :taskId")
    public Double findMaxSortIndexByTask(@Param("taskId") String taskId);
}
