import { ApiErrorResponse } from '@/lib/api/client';
import { createClient } from '@/lib/api/server';
import axios, { AxiosError } from 'axios';
import { NextResponse, type NextRequest } from 'next/server';

export async function POST(request: NextRequest) {
  const { name } = await request.json();
  const api = await createClient();

  try {
    const response = await api.post('/api/organizations', { name });

    return new NextResponse(JSON.stringify(response.data), { status: 201 });
  } catch (error) {
    if (axios.isAxiosError(error)) {
      const apiError = error as AxiosError<ApiErrorResponse>;
      const errorResponse = apiError.response?.data.errors[0];
      return new NextResponse(JSON.stringify(errorResponse), {
        status: apiError.response?.data.code,
      });
    }

    return new NextResponse('Something went wrong', { status: 500 });
  }
}

export async function GET() {
  const api = await createClient();

  try {
    const response = await api.get('/api/organizations');

    return new NextResponse(JSON.stringify(response.data), { status: 200 });
  } catch (error) {
    if (axios.isAxiosError(error)) {
      const apiError = error as AxiosError<ApiErrorResponse>;
      const errorResponse = apiError.response?.data.errors[0];
      return new NextResponse(JSON.stringify(errorResponse), {
        status: apiError.response?.data.code,
      });
    }

    return new NextResponse('Something went wrong', { status: 500 });
  }
}
