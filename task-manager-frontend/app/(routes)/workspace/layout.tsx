import { createClient } from '@/lib/api/server';
import { redirect } from 'next/navigation';
import React from 'react';

export default async function WorkspaceLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const api = await createClient();
  const response = await api.get('/api/organizations');
  const organizations = response.data;

  if (!Array.isArray(organizations) || organizations.length === 0) {
    redirect('/new-organization');
  }

  return <div>{children}</div>;
}
