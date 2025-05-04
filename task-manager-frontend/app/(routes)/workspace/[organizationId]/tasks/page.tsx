import React from 'react';

export default async function TasksPage({
  params,
}: {
  params: Promise<{ organizationId: string }>;
}) {
  const { organizationId } = await params;

  console.log(organizationId);

  return <div>TasksPage</div>;
}
