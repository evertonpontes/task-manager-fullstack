package com.everton.taskmanager.entities.groups.projects;

import com.everton.taskmanager.entities.groups.GroupMember;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "project_members")
@Getter
@Setter
@NoArgsConstructor
public class ProjectMember extends GroupMember<Project> {
}
