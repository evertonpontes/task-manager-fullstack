package com.everton.taskmanager.entities.groups.teams;

import com.everton.taskmanager.entities.attributes.Attribute;
import com.everton.taskmanager.entities.groups.Group;
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
@Table(name = "teams")
@SuperBuilder
@NoArgsConstructor
@Getter
public class Team extends Group {
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeamMember> members;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attribute> attributes;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    public boolean hasAccess(User user) {
        if (user == null) return false;

        boolean isOwner = this.isOwner(user);
        boolean isMember = this.getMembers().stream()
                .anyMatch(member -> member.getId().getMemberId().equals(user.getId()));

        return isOwner || isMember;
    }
}
