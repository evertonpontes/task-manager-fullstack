package com.everton.taskmanager.repositories;

import com.everton.taskmanager.entities.groups.teams.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, String> {
    @Query("SELECT COALESCE(MAX(m.sortIndex), 0) FROM TeamMember m WHERE m.group.id = :groupId")
    public Double findMaxSortIndexByGroupId(@Param("groupId") String groupId);

    @Query("SELECT COALESCE(MAX(t.sortIndex), 0) FROM Team t WHERE t.organization.id = :groupId")
    public Double findMaxSortIndexOfTeam(@Param("groupId") String groupId);

    public List<Team> findAllByOwnerId(String ownerId);
}
