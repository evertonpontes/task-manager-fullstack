'use client';

import React from 'react';
import { Modal } from '@/components/ui/modal';
import { OrganizationForm } from '@/components/organization/organization-form';
import { useOrganizationModal } from '@/hooks/use-organization-modal';

export const OrganizationModal = () => {
  const { isOpen, onClose } = useOrganizationModal();

  return (
    <Modal
      title="Create organization"
      description="Add a new organization to manage tasks and projects."
      isOpen={isOpen}
      onClose={onClose}
    >
      <div className="grid gap-4 py-4">
        <OrganizationForm />
      </div>
    </Modal>
  );
};
