'use client';

import React from 'react';
import { Modal } from '@/components/ui/modal';
import { TeamForm } from '@/components/team/team-form';
import { useTeamModal } from '@/hooks/use-team-modal';

export const TeamModal = () => {
  const { isOpen, onClose } = useTeamModal();

  return (
    <Modal
      title="Create team"
      description="Add a new team to manage tasks and projects."
      isOpen={isOpen}
      onClose={onClose}
    >
      <div className="grid gap-4 py-4">
        <TeamForm />
      </div>
    </Modal>
  );
};
