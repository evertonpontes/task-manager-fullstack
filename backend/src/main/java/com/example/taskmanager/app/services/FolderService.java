package com.example.taskmanager.app.services;

import com.example.taskmanager.app.dtos.folder.SaveFolderRequestDTO;
import com.example.taskmanager.app.dtos.folder.FolderResponseDTO;
import com.example.taskmanager.app.entities.Folder;
import com.example.taskmanager.app.mappers.FolderMapper;
import com.example.taskmanager.app.repositories.FolderRepository;
import com.example.taskmanager.user.entities.User;
import com.example.taskmanager.user.services.AuthService;
import com.example.taskmanager.utils.exceptions.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;
    private final FolderMapper folderMapper;
    private final AuthService authService;

    public FolderResponseDTO createFolder(SaveFolderRequestDTO request) {
        String name = request.name().trim();

        User user = authService.getAuthenticatedUser();

        BigDecimal maxSortIndex = folderRepository.findTopByUserIdSortBySortIndexDesc(user.getId()).orElse(BigDecimal.valueOf(-1.0));
        BigDecimal newSortIndex = maxSortIndex.add(BigDecimal.valueOf(1.0));

        Folder folder = folderRepository.save(Folder.builder()
                        .name(name)
                        .user(user)
                        .sortIndex(newSortIndex)
                .build());

        return folderMapper.folderToFolderResponseDTO(folder);
    }

    public List<FolderResponseDTO> getAllUserFolders() {
        User user = authService.getAuthenticatedUser();
        List<Folder> folders = folderRepository.findAllByUserId(user.getId());
        return folders.stream()
                .map(folderMapper::folderToFolderResponseDTO)
                .collect(Collectors.toList());
    }

    public FolderResponseDTO getFolderById(UUID id) {
        User user = authService.getAuthenticatedUser();
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found with id: " + id));
        
        if (!folder.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to access this folder");
        }
        
        return folderMapper.folderToFolderResponseDTO(folder);
    }

    public FolderResponseDTO updateFolderName(UUID id, SaveFolderRequestDTO request) {
        String newName = request.name().trim();

        User user = authService.getAuthenticatedUser();
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found with id: " + id));

        if (!folder.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to update this folder");
        }

        folder.setName(newName);
        Folder updatedFolder = folderRepository.save(folder);
        
        return folderMapper.folderToFolderResponseDTO(updatedFolder);
    }

    public void deleteFolder(UUID id) {
        User user = authService.getAuthenticatedUser();
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found with id: " + id));

        if (!folder.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to delete this folder");
        }

        folderRepository.delete(folder);
    }
}
