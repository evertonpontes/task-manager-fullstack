package com.everton.taskmanager.entities.groups.organizations;

import com.everton.taskmanager.entities.groups.GroupMember;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organization_members")
@Getter
@Setter
@NoArgsConstructor
public class OrganizationMember extends GroupMember<Organization> {
}
