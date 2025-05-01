package com.everton.taskmanager.entities.tasks;

import com.everton.taskmanager.entities.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalTime;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class GenericTask {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "sort_index")
    private Double sortIndex;

    private String title;

    @Column(name = "due_date")
    private Instant dueDate;

    @Column(name = "estimated_time")
    private LocalTime estimatedTime;

    @ManyToOne
    @JoinColumn(name = "assignee_id", nullable = false)
    private User assignee;
}
