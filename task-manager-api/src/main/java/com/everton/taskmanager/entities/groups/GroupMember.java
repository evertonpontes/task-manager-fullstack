package com.everton.taskmanager.entities.groups;

import com.everton.taskmanager.entities.users.User;
import jakarta.persistence.*;
import lombok.*;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class GroupMember<G extends Group> {
    @EmbeddedId
    private GroupMemberId id;

    @ManyToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private User member;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private G group;

    @Column(name = "sort_index")
    private Double sortIndex;
}
