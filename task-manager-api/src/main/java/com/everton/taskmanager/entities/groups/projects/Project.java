package com.everton.taskmanager.entities.groups.projects;

import com.everton.taskmanager.entities.attributes.Attribute;
import com.everton.taskmanager.entities.groups.Group;
import com.everton.taskmanager.entities.groups.folders.Folder;
import com.everton.taskmanager.entities.groups.organizations.Organization;
import com.everton.taskmanager.entities.tasks.Task;
import com.everton.taskmanager.entities.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "projects")
@SuperBuilder
@NoArgsConstructor
@Getter
public class Project extends Group {
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectMember> members;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attribute> attributes;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    public boolean hasAccess(User user) {
        if (user == null) return false;

        boolean isOwner = this.isOwner(user);
        boolean isMember = this.getMembers().stream()
                .anyMatch(member -> member.getId().getMemberId().equals(user.getId()));

        return isOwner || isMember;
    }
}
