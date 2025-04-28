package com.everton.taskmanager.entities.groups.projects;

import com.everton.taskmanager.entities.attributes.Attribute;
import com.everton.taskmanager.entities.groups.Group;
import com.everton.taskmanager.entities.groups.folders.Folder;
import com.everton.taskmanager.entities.groups.organizations.Organization;
import com.everton.taskmanager.entities.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "projects")
@SuperBuilder
@NoArgsConstructor
@Getter
public class Project extends Group {

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "folder_id", nullable = true)
    private Folder folder;

    @ManyToMany()
    @JoinTable(name = "project_members",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<User> members;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attribute> attributes;
}
