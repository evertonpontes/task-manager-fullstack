package com.everton.taskmanager.entities.groups.folders;

import com.everton.taskmanager.entities.attributes.Attribute;
import com.everton.taskmanager.entities.groups.Group;
import com.everton.taskmanager.entities.groups.organizations.Organization;
import com.everton.taskmanager.entities.groups.projects.Project;
import com.everton.taskmanager.entities.tasks.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "folders")
@SuperBuilder
@NoArgsConstructor
@Getter
public class Folder extends Group {
    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attribute> attributes;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Project> projects;
}
