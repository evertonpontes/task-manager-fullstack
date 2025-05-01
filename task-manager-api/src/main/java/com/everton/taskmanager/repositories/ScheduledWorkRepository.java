package com.everton.taskmanager.repositories;

import com.everton.taskmanager.entities.tasks.tasktiming.ScheduledWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduledWorkRepository extends JpaRepository<ScheduledWork, String> {
    @Query("SELECT COALESCE(MAX(sw.sortIndex), 0) FROM ScheduledWork sw WHERE sw.task.id = :taskId")
    public Double findMaxSortIndexByTask(@Param("taskId") String taskId);
}
