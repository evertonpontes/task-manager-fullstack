package com.everton.taskmanager.repositories;

import com.everton.taskmanager.entities.groups.organizations.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, String> {
    @Query("SELECT COALESCE(MAX(m.sortIndex), 0) FROM OrganizationMember m WHERE m.group.id = :groupId")
    public Double findMaxSortIndexByGroupId(@Param("groupId") String groupId);

    @Query("SELECT COALESCE(MAX(o.sortIndex), 0) FROM Project o WHERE o.owner.id = :ownerId")
    public Double findMaxSortIndexByOwner(@Param("ownerId") String ownerId);

    public List<Organization> findAllByOwnerId(String ownerId);
}
