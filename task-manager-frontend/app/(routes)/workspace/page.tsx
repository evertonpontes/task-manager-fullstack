import { createClient } from '@/lib/api/server';
import { redirect } from 'next/navigation';

type Organization = {
  id: string;
  name: string;
};

export default async function WorkspacePage() {
  const api = await createClient();

  const response = await api.get('/api/organizations');
  const organizations = response.data as Organization[];

  if (Array.isArray(organizations) && organizations.length > 0) {
    redirect(`/workspace/${organizations[0].id}/tasks?view=TableView`);
  } else {
    redirect('/new-organization');
  }
}
