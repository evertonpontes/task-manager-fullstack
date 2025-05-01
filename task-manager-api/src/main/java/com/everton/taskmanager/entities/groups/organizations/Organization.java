package com.everton.taskmanager.entities.groups.organizations;

import com.everton.taskmanager.entities.attributes.Attribute;
import com.everton.taskmanager.entities.groups.Group;
import com.everton.taskmanager.entities.groups.folders.Folder;
import com.everton.taskmanager.entities.groups.projects.Project;
import com.everton.taskmanager.entities.groups.teams.Team;
import com.everton.taskmanager.entities.tasks.Task;
import com.everton.taskmanager.entities.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "organizations")
@SuperBuilder
@NoArgsConstructor
@Getter
public class Organization extends Group {
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrganizationMember> members;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attribute> attributes;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Team> teams;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Folder> folders;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Project> projects;

    public boolean hasAccess(User user) {
        if (user == null) return false;

        boolean isOwner = this.isOwner(user);
        boolean isMember = this.getMembers().stream()
                .anyMatch(member -> member.getId().getMemberId().equals(user.getId()));

        return isOwner || isMember;
    }
}
