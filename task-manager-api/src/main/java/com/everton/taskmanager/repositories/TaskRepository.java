package com.everton.taskmanager.repositories;

import com.everton.taskmanager.entities.tasks.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, String> {
    @Query("SELECT COALESCE(MAX(t.sortIndex), 0) FROM Task t WHERE t.organization.id = :groupId")
    public Double findMaxSortIndexByOrganization(@Param("groupId") String groupId);

    @Query("SELECT COALESCE(MAX(t.sortIndex), 0) FROM Task t WHERE t.team.id = :groupId")
    public Double findMaxSortIndexByTeam(@Param("groupId") String groupId);

    @Query("SELECT COALESCE(MAX(t.sortIndex), 0) FROM Task t WHERE t.folder.id = :groupId")
    public Double findMaxSortIndexByFolder(@Param("groupId") String groupId);

    @Query("SELECT COALESCE(MAX(t.sortIndex), 0) FROM Task t WHERE t.project.id = :groupId")
    public Double findMaxSortIndexByProject(@Param("groupId") String groupId);
}
