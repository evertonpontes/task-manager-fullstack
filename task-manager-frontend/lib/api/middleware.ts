import axios from 'axios';
import { NextResponse, type NextRequest } from 'next/server';

type AuthUser = {
  id: string;
  name: string;
  email: string;
};

export async function updateSession(request: NextRequest) {
  const token = request.cookies.get('access-token')?.value;

  if (!token && request.nextUrl.pathname.startsWith('/workspace')) {
    const url = request.nextUrl.clone();
    url.pathname = '/sign-in';
    return NextResponse.redirect(url);
  }

  const api = axios.create({
    baseURL: process.env.NEXT_PUBLIC_TASK_MANAGER_API_BASE_URL,
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    withCredentials: true,
  });

  try {
    await api.get<AuthUser>('/auth/me');

    return NextResponse.next({
      request,
    });
  } catch (error) {
    if (request.nextUrl.pathname.startsWith('/workspace')) {
      const url = request.nextUrl.clone();
      url.pathname = '/sign-in';
      return NextResponse.redirect(url);
    }

    console.log('Something went wrong', error);
  }

  return NextResponse.next({
    request,
  });
}
