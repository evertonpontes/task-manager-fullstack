package com.everton.taskmanager.entities.attributes;

import com.everton.taskmanager.entities.groups.folders.Folder;
import com.everton.taskmanager.entities.groups.organizations.Organization;
import com.everton.taskmanager.entities.groups.projects.Project;
import com.everton.taskmanager.entities.groups.teams.Team;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attributes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String color;

    @Enumerated(EnumType.STRING)
    private AttributeTypeEnum type;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;
}
