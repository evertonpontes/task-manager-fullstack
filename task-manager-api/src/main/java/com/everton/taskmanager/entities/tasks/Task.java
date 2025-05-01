package com.everton.taskmanager.entities.tasks;

import com.everton.taskmanager.entities.groups.Group;
import com.everton.taskmanager.entities.groups.folders.Folder;
import com.everton.taskmanager.entities.groups.organizations.Organization;
import com.everton.taskmanager.entities.groups.projects.Project;
import com.everton.taskmanager.entities.groups.teams.Team;
import com.everton.taskmanager.entities.tasks.tasktiming.LoggedTime;
import com.everton.taskmanager.entities.tasks.tasktiming.ScheduledWork;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "tasks")
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class Task extends GenericTask{
    private String description;

    private String status;

    private String type;

    private String priority;

    @ElementCollection
    @CollectionTable(name = "task_attached_files",
            joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "attached_file")
    private List<String> attachedFiles;

    private Boolean repeatTask;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubTask> subTasks;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduledWork> scheduledWorks;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoggedTime> loggedTimes;
}
