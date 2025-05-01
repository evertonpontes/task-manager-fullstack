package com.everton.taskmanager.entities.tasks.tasktiming;

import com.everton.taskmanager.entities.tasks.Task;
import com.everton.taskmanager.entities.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalTime;

@Entity
@Table(name = "scheduled_works")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScheduledWork {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "sort_index")
    private Double sortIndex;

    private Instant date;

    @Column(name = "estimated_time")
    private LocalTime estimatedTime;

    @ManyToOne
    @JoinColumn(name = "assignee_id", nullable = false)
    private User assignee;

    private Boolean completed;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
}
