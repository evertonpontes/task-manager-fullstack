package com.everton.taskmanager.entities.attributes.taskStatus;

import com.everton.taskmanager.entities.attributes.Attribute;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "task_statuses")
@SuperBuilder
@NoArgsConstructor
public class TaskStatus extends Attribute {

}
