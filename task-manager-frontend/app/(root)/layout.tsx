import { createClient } from '@/lib/api/server';
import { redirect } from 'next/navigation';
import React from 'react';

export default async function SetupLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const api = await createClient();

  const response = await api.get('/api/organizations');

  const organizations = response.data;

  if (Array.isArray(organizations) && organizations.length > 0) {
    redirect(`/workspace/${organizations[0].id}/tasks?view=TableView`);
  }

  return <div>{children}</div>;
}
