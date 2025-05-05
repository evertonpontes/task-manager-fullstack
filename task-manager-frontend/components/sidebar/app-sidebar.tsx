'use client';

import React, { useEffect } from 'react';
import {
  Sidebar,
  SidebarContent,
  SidebarHeader,
} from '@/components/ui/sidebar';
import { useOrganization } from '@/hooks/use-organization';
import { HeaderSidebar } from '@/components/sidebar/header-sidebar';
import { NavMain } from '@/components/sidebar/nav-main';

import { BriefcaseBusiness, Dumbbell } from 'lucide-react';

export const AppSidebar = ({
  ...props
}: React.ComponentProps<typeof Sidebar>) => {
  const { organizations, setOrganizations } = useOrganization();

  useEffect(() => {
    const fetchOrganizations = async () => {
      const response = await fetch('/api/organizations');
      if (response.ok) {
        const organizations = await response.json();

        setOrganizations(organizations);
      }
    };

    fetchOrganizations();
  }, [setOrganizations]);

  const data = {
    search: [
      {
        id: 'my work',
        title: 'My Work',
        icon: Dumbbell,
        href: '/tasks?view=TableView',
      },
      {
        id: 'all projects',
        title: 'All projects',
        icon: BriefcaseBusiness,
        href: '/projects',
      },
    ],
    teams: [],
  };

  return (
    <Sidebar collapsible="offcanvas" {...props}>
      <SidebarHeader>
        <HeaderSidebar />
      </SidebarHeader>
      <SidebarContent>
        <NavMain />
      </SidebarContent>
    </Sidebar>
  );
};
