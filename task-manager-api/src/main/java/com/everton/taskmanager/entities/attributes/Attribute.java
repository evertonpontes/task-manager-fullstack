package com.everton.taskmanager.entities.attributes;

import com.everton.taskmanager.entities.organization.Organization;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String color;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;
}
