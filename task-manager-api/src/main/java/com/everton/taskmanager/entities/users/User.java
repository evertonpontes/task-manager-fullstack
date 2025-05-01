package com.everton.taskmanager.entities.users;

import com.everton.taskmanager.entities.groups.folders.Folder;
import com.everton.taskmanager.entities.groups.organizations.Organization;
import com.everton.taskmanager.entities.groups.organizations.OrganizationMember;
import com.everton.taskmanager.entities.groups.projects.Project;
import com.everton.taskmanager.entities.groups.projects.ProjectMember;
import com.everton.taskmanager.entities.groups.teams.Team;
import com.everton.taskmanager.entities.groups.teams.TeamMember;
import com.everton.taskmanager.entities.tasks.SubTask;
import com.everton.taskmanager.entities.tasks.Task;
import com.everton.taskmanager.entities.tasks.tasktiming.LoggedTime;
import com.everton.taskmanager.entities.tasks.tasktiming.ScheduledWork;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Organization> ownedOrganizations;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Team> ownedTeams;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Folder> ownedFolders;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Project> ownedProjects;

    @OneToMany(mappedBy = "member")
    private Set<OrganizationMember> organizations;

    @OneToMany(mappedBy = "member")
    private Set<ProjectMember> projects;

    @OneToMany(mappedBy = "member")
    private Set<TeamMember> teams;

    @OneToMany(mappedBy = "assignee")
    private List<Task> tasks;

    @OneToMany(mappedBy = "assignee")
    private List<SubTask> subTasks;

    @OneToMany(mappedBy = "assignee")
    private List<ScheduledWork> scheduleWorks;

    @OneToMany(mappedBy = "assignee")
    private List<LoggedTime> loggedTimes;

}
