'use client';

import { useOrganizationModal } from '@/hooks/use-organization-modal';
import React, { useEffect } from 'react';

export default function OrganizationPage() {
  const isOpen = useOrganizationModal((state) => state.isOpen);
  const onOpen = useOrganizationModal((state) => state.onOpen);
  const onClose = useOrganizationModal((state) => state.onClose);

  useEffect(() => {
    if (!isOpen) {
      onOpen();
    }
  }, [isOpen, onOpen]);

  useEffect(() => {
    return () => {
      onClose();
    };
  }, [onClose]);

  return <div className="sr-only">This page is protected</div>;
}
