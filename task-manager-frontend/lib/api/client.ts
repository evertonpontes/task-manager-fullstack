import axios from 'axios';

export const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_TASK_MANAGER_API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});

export interface ApiErrorResponse {
  timestamp: Date;
  code: number;
  status: string;
  errors: string[];
}
