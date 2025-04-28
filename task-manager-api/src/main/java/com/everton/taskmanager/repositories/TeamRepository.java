package com.everton.taskmanager.repositories;

import com.everton.taskmanager.entities.groups.teams.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, String> {
}
