package com.everton.taskmanager.repositories;

import com.everton.taskmanager.entities.groups.folders.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, String> {
}
