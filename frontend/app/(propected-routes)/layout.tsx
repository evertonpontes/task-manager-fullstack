import { UserResponse } from '@/hooks/useAuth';
import { createServer } from '@/lib/api/server';
import { AuthProvider } from '@/providers/auth-provider';
import { cookies } from 'next/headers';
import { redirect } from 'next/navigation';
import React from 'react';

export default async function ProtectedLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const server = await createServer();
  const token = (await cookies()).get('access-token')?.value;
  let user = null;

  try {
    const response = await server.get<UserResponse>('/api/users', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      withCredentials: true,
    });

    user = response.data;
  } catch (error) {
    if (error instanceof Error) {
      console.log(error.message);
    }

    redirect('/auth/sign-in');
  }

  return (
    <div>
      <AuthProvider user={user} token={token}>
        {children}
      </AuthProvider>
    </div>
  );
}
