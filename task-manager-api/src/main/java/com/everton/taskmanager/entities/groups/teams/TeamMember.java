package com.everton.taskmanager.entities.groups.teams;

import com.everton.taskmanager.entities.groups.GroupMember;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "team_members")
@Getter
@Setter
@NoArgsConstructor
public class TeamMember extends GroupMember<Team> {
}
