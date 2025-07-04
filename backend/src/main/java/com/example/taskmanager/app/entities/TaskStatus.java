package com.example.taskmanager.app.entities;

import com.example.taskmanager.app.entities.task.RecurrenceRule;
import com.example.taskmanager.app.entities.task.RepeatableRule;
import com.example.taskmanager.app.entities.task.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Table(name = "task_statuses")
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskStatus extends TaskAttribute {
    @Enumerated(EnumType.STRING)
    private TaskStatusKindEnum kind;
    private Boolean deletable;
    private Boolean draggable;
    @OneToMany(mappedBy = "taskStatus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    @OneToMany(mappedBy = "initialTaskStatus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecurrenceRule> recurrenceRules;

    @OneToMany(mappedBy = "initialTaskStatus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepeatableRule> repeatableRules;
}
