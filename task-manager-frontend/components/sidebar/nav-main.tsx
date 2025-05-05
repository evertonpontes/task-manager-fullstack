'use client';

import { BriefcaseBusiness, Dumbbell, Search } from 'lucide-react';
import React, { useEffect, useState } from 'react';
import {
  SidebarGroup,
  SidebarGroupContent,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from '@/components/ui/sidebar';
import { useParams } from 'next/navigation';
import { CollapsibleItem } from '@/components/sidebar/collapsible-item';
import { useTeamModal } from '@/hooks/use-team-modal';
import { useTeam } from '@/hooks/use-team';
import { Button } from '../ui/button';

const SearchSidebar = () => {
  const params = useParams<{ organizationId: string }>();
  const organizationId = params?.organizationId;
  const pathname = `/workspace/${organizationId}`;

  const data = [
    {
      id: 'my work',
      title: 'My Work',
      icon: Dumbbell,
      href: `${pathname}/tasks?view=TableView`,
    },
    {
      id: 'all projects',
      title: 'All projects',
      icon: BriefcaseBusiness,
      href: `${pathname}/projects`,
    },
  ];

  return (
    <SidebarGroup>
      <SidebarGroupContent className="space-y-2">
        <SidebarMenu>
          <SidebarMenuItem>
            <div className="px-3 py-1 flex items-center gap-2 border border-input w-full min-w-0 rounded-md text-base shadow-xs">
              <Search className="size-4" />
              <input
                placeholder="Search"
                className="w-full outline-0 bg-transparent"
              />
            </div>
          </SidebarMenuItem>
        </SidebarMenu>
        <SidebarMenu className="space-y-2">
          {data.map((item) => (
            <SidebarMenuItem key={item.id}>
              <SidebarMenuButton>
                <a href={item.href} className="flex items-center gap-2">
                  <item.icon className="shrink-0" />
                  {item.title}
                </a>
              </SidebarMenuButton>
            </SidebarMenuItem>
          ))}
        </SidebarMenu>
      </SidebarGroupContent>
    </SidebarGroup>
  );
};

const TeamsSidebar = () => {
  const onOpen = useTeamModal((state) => state.onOpen);
  const params = useParams<{ organizationId: string }>();
  const organizationId = params?.organizationId;
  const setTeams = useTeam((state) => state.setTeams);
  const teams = useTeam((state) => state.teams);

  useEffect(() => {
    async function fetchTeamsByOrganizationId() {
      const response = await fetch(
        `/api/organizations/${organizationId}/teams`,
        {
          method: 'GET',
        }
      );

      if (response.ok) {
        const teams = await response.json();
        setTeams(teams);
      }
    }

    fetchTeamsByOrganizationId();
  }, [organizationId, setTeams]);

  return (
    <SidebarGroup>
      <SidebarGroupContent>
        <SidebarMenu>
          <SidebarMenuItem>
            <CollapsibleItem text="Teams" onClick={onOpen}>
              {teams.map((team) => (
                <Button
                  key={team.id}
                  variant="ghost"
                  className="w-full justify-start"
                >
                  {team.name}
                </Button>
              ))}
            </CollapsibleItem>
          </SidebarMenuItem>
        </SidebarMenu>
      </SidebarGroupContent>
    </SidebarGroup>
  );
};

export const NavMain = () => {
  return (
    <>
      <SearchSidebar />
      <TeamsSidebar />
    </>
  );
};
