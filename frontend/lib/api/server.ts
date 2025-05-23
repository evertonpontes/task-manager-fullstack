import axios from 'axios';
import { cookies } from 'next/headers';

export async function createServer() {
  const cookieStore = await cookies();

  const api = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      Authorization: `Bearer ${cookieStore.get('access-token')?.value}`,
    },
    withCredentials: true,
  });

  return api;
}
