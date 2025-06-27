package com.example.taskmanager.app.entities;

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
@Table(name = "task_types")
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class TaskType extends TaskAttribute {
    @OneToMany(mappedBy = "taskType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;
}
