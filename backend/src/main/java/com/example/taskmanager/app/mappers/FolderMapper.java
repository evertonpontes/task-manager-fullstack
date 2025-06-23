package com.example.taskmanager.app.mappers;

import com.example.taskmanager.app.dtos.folder.FolderResponseDTO;
import com.example.taskmanager.app.entities.Folder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FolderMapper {
    FolderResponseDTO folderToFolderResponseDTO(Folder folder);
}
