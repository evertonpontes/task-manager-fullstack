'use client';
import { useAuth } from '@/hooks/useAuth';
import React from 'react';

export default function WorkspacePage() {
  const user = useAuth((state) => state.user);

  return (
    <div>
      <div>{user?.firstName}</div>
      <div>{user?.lastName}</div>
      <div>{user?.email}</div>
      <div>{user?.role}</div>
    </div>
  );
}
