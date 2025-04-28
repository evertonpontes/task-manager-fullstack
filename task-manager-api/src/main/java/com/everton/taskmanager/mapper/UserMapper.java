package com.everton.taskmanager.mapper;

import com.everton.taskmanager.dtos.user.CreateUserDTO;
import com.everton.taskmanager.dtos.user.UserResponseDTO;
import com.everton.taskmanager.entities.users.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    public User createUserDTOToUser(CreateUserDTO userDTO);

    public UserResponseDTO userToUserResponseDTO(User user);

}
