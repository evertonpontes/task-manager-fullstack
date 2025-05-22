import { StartForm } from '@/components/auth/start-form';
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import Link from 'next/link';
import React from 'react';

export default function StartPage() {
  return (
    <div className="flex flex-col gap-6">
      <Card>
        <CardHeader>
          <CardTitle className="text-2xl">Welcome to Task Manager</CardTitle>
          <CardDescription>Get started by creating a account</CardDescription>
        </CardHeader>
        <CardContent>
          <StartForm />
        </CardContent>
        <CardFooter>
          <div className="text-center text-sm">
            Already have an account?{' '}
            <Link
              href={'/auth/sign-in'}
              className="underline underline-offset-2"
            >
              Sign in
            </Link>
          </div>
        </CardFooter>
      </Card>
    </div>
  );
}
