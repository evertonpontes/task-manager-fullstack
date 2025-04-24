package com.everton.taskmanager.entities.teams;

import com.everton.taskmanager.entities.attributes.eventType.EventType;
import com.everton.taskmanager.entities.attributes.taskStatus.TaskStatus;
import com.everton.taskmanager.entities.attributes.taskType.TaskType;
import com.everton.taskmanager.entities.organization.Organization;
import com.everton.taskmanager.entities.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "teams")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToMany
    @JoinTable(name = "team_attendees",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> attendees;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskType> taskTypes;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventType> eventTypes;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskStatus> taskStatuses;
}
