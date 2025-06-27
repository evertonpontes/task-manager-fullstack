package com.example.taskmanager.app.entities;

import com.example.taskmanager.app.entities.task.ScheduledRepeat;
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
    private Boolean isTaskCompleted;
    @OneToMany(mappedBy = "task_status", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    @OneToMany(mappedBy = "default_status", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduledRepeat> scheduledRepeats;
}
