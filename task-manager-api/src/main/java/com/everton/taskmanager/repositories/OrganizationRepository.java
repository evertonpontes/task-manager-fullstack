package com.everton.taskmanager.repositories;

import com.everton.taskmanager.entities.organization.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String> {

    @Query("SELECT org FROM Organization org WHERE org.user.id = :userId")
    public Organization findOrganizationByUserId(@Param("userId") String userId);

    @Query("SELECT org FROM Organization org WHERE org.user.id = :userId and org.name = :name")
    public Organization findOrganizationByUserIdAndName(@Param("userId") String userId, @Param("name") String name);
}
