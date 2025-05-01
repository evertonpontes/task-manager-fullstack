package com.everton.taskmanager.repositories;

import com.everton.taskmanager.entities.groups.folders.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FolderRepository extends JpaRepository<Folder, String> {
    @Query("SELECT COALESCE(MAX(f.sortIndex), 0) FROM Folder f WHERE f.organization.id = :groupId")
    public Double findMaxSortIndexByGroupId(@Param("groupId") String groupId);
}
