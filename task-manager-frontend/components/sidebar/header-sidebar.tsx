'use client';

import { useOrganization } from '@/hooks/use-organization';
import { useParams } from 'next/navigation';
import React, { useState } from 'react';
import {
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from '@/components/ui/sidebar';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import {
  ChevronDown,
  ChevronUp,
  FolderCog,
  Settings,
  SquarePlus,
  Users,
} from 'lucide-react';
import { useOrganizationModal } from '@/hooks/use-organization-modal';

export const HeaderSidebar = () => {
  const [open, setOpen] = useState(false);
  const { organizations, getOrganizationById } = useOrganization();
  const { isOpen, onOpen } = useOrganizationModal();
  const params = useParams<{ organizationId: string }>();
  const organizationId = params?.organizationId;
  const organization = getOrganizationById(organizationId!);

  const switchToOrganizations = () => {
    return organizations.filter((org) => org.id !== organizationId);
  };

  const handleOpenModal = () => {
    if (!isOpen) {
      onOpen();
    }
  };

  return (
    <SidebarMenu>
      <SidebarMenuItem>
        <DropdownMenu open={open} onOpenChange={setOpen}>
          <DropdownMenuTrigger asChild>
            <SidebarMenuButton className="flex justify-between cursor-pointer">
              <div className="flex items-center gap-2">
                <FolderCog />
                {organization ? (
                  <span className="truncate">{organization?.name}</span>
                ) : (
                  <span className="truncate">
                    Connecting to organization...
                  </span>
                )}
              </div>
              {open ? <ChevronUp /> : <ChevronDown />}
            </SidebarMenuButton>
          </DropdownMenuTrigger>
          <DropdownMenuContent className="w-60 grid gap-1 py-1.5 px-2">
            <DropdownMenuLabel>{organization?.name}</DropdownMenuLabel>
            <DropdownMenuSeparator />
            <DropdownMenuGroup>
              <DropdownMenuItem className="py-2 px-2.5">
                <a href="#" className="flex items-center gap-2 w-full">
                  <Settings />
                  Organization settings
                </a>
              </DropdownMenuItem>
              <DropdownMenuItem className="py-2 px-2.5">
                <a href="#" className="flex items-center gap-2 w-full">
                  <Users />
                  Teams & Users
                </a>
              </DropdownMenuItem>
            </DropdownMenuGroup>
            {switchToOrganizations().length > 0 && (
              <>
                <DropdownMenuSeparator />
                <DropdownMenuLabel>Switch to</DropdownMenuLabel>
                <DropdownMenuGroup>
                  {switchToOrganizations().map((org) => (
                    <DropdownMenuItem key={org.id} className="py-2 px-2.5">
                      <a
                        href={`/workspace/${org.id}/tasks?view=TableView`}
                        className="flex items-center gap-2 w-full"
                      >
                        {org.name}
                      </a>
                    </DropdownMenuItem>
                  ))}
                </DropdownMenuGroup>
              </>
            )}
            <DropdownMenuSeparator />
            <DropdownMenuGroup>
              <DropdownMenuItem className="py-2 px-2.5">
                <button
                  onClick={handleOpenModal}
                  className="w-full flex items-center gap-2 cursor-pointer"
                >
                  <SquarePlus />
                  Create organization
                </button>
              </DropdownMenuItem>
            </DropdownMenuGroup>
          </DropdownMenuContent>
        </DropdownMenu>
      </SidebarMenuItem>
    </SidebarMenu>
  );
};
