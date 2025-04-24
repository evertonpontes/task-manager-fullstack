package com.everton.taskmanager.repositories;

import com.everton.taskmanager.entities.teams.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {

}
