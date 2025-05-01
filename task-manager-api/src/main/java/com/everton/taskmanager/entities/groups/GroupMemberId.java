package com.everton.taskmanager.entities.groups;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberId implements Serializable {
    @Column(name = "member_id")
    private String memberId;

    @Column(name = "group_id")
    private String groupId;
}
