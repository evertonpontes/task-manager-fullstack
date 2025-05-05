'use client';

import { OrganizationModal } from '@/components/organization/organization-modal';
import { TeamModal } from '@/components/team/team-modal';
import React, { useEffect, useState } from 'react';

export const ModalProvider = () => {
  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true);
  }, []);

  if (!isMounted) {
    return null;
  }

  return (
    <>
      <OrganizationModal />
      <TeamModal />
    </>
  );
};
