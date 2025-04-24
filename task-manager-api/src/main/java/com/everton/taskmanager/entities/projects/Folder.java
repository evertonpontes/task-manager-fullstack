package com.everton.taskmanager.entities.projects;

import com.everton.taskmanager.entities.organization.Organization;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "folders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;
}
