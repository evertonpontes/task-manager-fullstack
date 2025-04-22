package com.everton.taskmanager.mapper;

import com.everton.taskmanager.entities.user.RecoveryUserDTO;
import com.everton.taskmanager.entities.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    RecoveryUserDTO userToRecoveryUserDTO(User user);
}
