package com.example.taskmanager.app.mappers;

import com.example.taskmanager.app.dtos.FolderResponseDTO;
import com.example.taskmanager.app.entities.Folder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FolderMapper {
    public FolderResponseDTO folderToFolderResponseDTO(Folder folder);
}
