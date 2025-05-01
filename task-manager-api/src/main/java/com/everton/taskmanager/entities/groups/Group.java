package com.everton.taskmanager.entities.groups;

import com.everton.taskmanager.entities.attributes.Attribute;
import com.everton.taskmanager.entities.tasks.Task;
import com.everton.taskmanager.entities.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "sort_index")
    private Double sortIndex;

    public boolean isOwner(User user) {
        if (user == null) return false;

        return this.getOwner().getId().equals(user.getId());
    }
}
