package com.everton.taskmanager.dtos.groups;

import com.everton.taskmanager.dtos.user.UserResponseDTO;

public record GroupMemberResponseDTO (
        String id,
        Double sortIndex,
        UserResponseDTO memberDetails
) {
}
