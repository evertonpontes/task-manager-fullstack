import { ApiErrorResponse } from '@/lib/api/client';
import { createClient } from '@/lib/api/server';
import axios, { AxiosError } from 'axios';
import { NextResponse, type NextRequest } from 'next/server';

export async function POST(
  request: NextRequest,
  { params }: { params: { organizationId: string } }
) {
  const { name } = await request.json();
  const api = await createClient();
  const { organizationId } = params;

  try {
    const response = await api.post(
      `/api/organizations/${organizationId}/teams`,
      { name }
    );

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

export async function GET(
  request: NextRequest,
  { params }: { params: { organizationId: string } }
) {
  const api = await createClient();
  const { organizationId } = params;

  try {
    const response = await api.get(
      `/api/organizations/${organizationId}/teams`
    );

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
