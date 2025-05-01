package com.everton.taskmanager.entities.tasks;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "sub_tasks")
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class SubTask extends GenericTask{
    private Boolean completed;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
}
