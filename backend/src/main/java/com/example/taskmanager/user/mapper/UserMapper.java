package com.example.taskmanager.user.mapper;

import com.example.taskmanager.user.dtos.UserResponse;
import com.example.taskmanager.user.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse userToResponseData(User user);
}
