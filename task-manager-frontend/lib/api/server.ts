import axios from 'axios';
import { cookies } from 'next/headers';

export async function createClient() {
  const cookieStore = await cookies();

  const token = cookieStore.get('access-token')?.value;

  const api = axios.create({
    baseURL: process.env.NEXT_PUBLIC_TASK_MANAGER_API_BASE_URL,
    headers: {
      'Content-Type': 'application/json',
    },
    withCredentials: true,
  });

  api.interceptors.request.use((config) => {
    config.headers.Authorization = token ? `Bearer ${token}` : undefined;
    return config;
  });

  return api;
}
