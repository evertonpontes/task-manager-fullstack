package com.everton.taskmanager.entities.attributes.taskType;

import com.everton.taskmanager.entities.attributes.Attribute;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "task_types")
@SuperBuilder
@NoArgsConstructor
public class TaskType extends Attribute {

}
