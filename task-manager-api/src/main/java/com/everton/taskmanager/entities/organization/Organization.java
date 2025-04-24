package com.everton.taskmanager.entities.organization;

import com.everton.taskmanager.entities.attributes.eventType.EventType;
import com.everton.taskmanager.entities.attributes.taskStatus.TaskStatus;
import com.everton.taskmanager.entities.attributes.taskType.TaskType;
import com.everton.taskmanager.entities.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "organizations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(name = "organization_attendees",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> attendees;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskType> taskTypes;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventType> eventTypes;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskStatus> taskStatuses;
}
