'use client';

import { Button } from '@/components/ui/button';
import { useAuth, useAutoRefreshToken, UserResponse } from '@/hooks/useAuth';
import axios from 'axios';
import React, { useEffect, useState } from 'react';

type AuthProviderProps = {
  user?: UserResponse;
  token?: string;
  children: React.ReactNode;
};

export const AuthProvider: React.FC<AuthProviderProps> = ({
  user,
  token,
  children,
}) => {
  const [isLoading, setIsLoading] = useState(true);
  const setUser = useAuth((state) => state.setUser);
  const setToken = useAuth((state) => state.setToken);

  useEffect(() => {
    setUser(user!);
    setToken(token!);
    setIsLoading(false);
  }, [setToken, setUser, token, user]);

  useAutoRefreshToken(token);

  if (isLoading) {
    return null;
  }

  const logout = async () => {
    await axios.delete<UserResponse>(
      `${process.env.NEXT_PUBLIC_API_URL}/api/auth/logout`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      }
    );
  };

  return (
    <div>
      <Button onClick={logout}>Logout</Button>
      {children}
    </div>
  );
};
