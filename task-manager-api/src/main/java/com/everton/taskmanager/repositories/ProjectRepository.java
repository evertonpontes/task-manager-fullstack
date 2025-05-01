package com.everton.taskmanager.repositories;

import com.everton.taskmanager.entities.groups.projects.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, String> {
    @Query("SELECT COALESCE(MAX(m.sortIndex), 0) FROM OrganizationMember m WHERE m.group.id = :groupId")
    public Double findMaxSortIndexByGroupId(@Param("groupId") String groupId);

    @Query("SELECT COALESCE(MAX(p.sortIndex), 0) FROM Project p WHERE p.organization.id = :groupId")
    public Double findMaxSortIndexOfProject(@Param("groupId") String groupId);

    public List<Project> findAllByOwnerId(String ownerId);
}
