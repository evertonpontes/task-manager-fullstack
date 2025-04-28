package com.everton.taskmanager.repositories;

import com.everton.taskmanager.entities.groups.organizations.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, String> {
    public List<Organization> findAllByOwnerId(String ownerId);
}
