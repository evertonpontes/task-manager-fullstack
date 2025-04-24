package com.everton.taskmanager.entities.attributes.eventType;

import com.everton.taskmanager.entities.attributes.Attribute;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "event_types")
@SuperBuilder
@NoArgsConstructor
public class EventType extends Attribute {

}
