import axios from 'axios';
import { NextResponse, type NextRequest } from 'next/server';

export async function updateSession(request: NextRequest) {
  const response = NextResponse.next({
    request,
  });

  const apiUrl = process.env.NEXT_PUBLIC_API_URL;
  const cookies = request.cookies;
  const accessToken = cookies.get('access-token')?.value;

  try {
    await axios.get(`${apiUrl}/api/users`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      withCredentials: true,
    });

    return response;
  } catch (error) {
    if (error instanceof Error) {
      console.log(error.message);
    }

    if (request.nextUrl.pathname.startsWith('/workspace')) {
      const url = request.nextUrl.clone();
      url.pathname = '/auth/sign-in';
      return NextResponse.redirect(url);
    }
  }
}
