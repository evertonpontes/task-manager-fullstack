import { SignUpForm } from '@/components/auth/sign-up-form';
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

export default async function SignUpPage({
  searchParams,
}: {
  searchParams: Promise<{ email: string }>;
}) {
  const { email } = await searchParams;

  return (
    <div className="flex flex-col gap-6">
      <Card>
        <CardHeader>
          <CardTitle className="text-2xl">Welcome to Task Manager</CardTitle>
          <CardDescription>Get started by creating a account</CardDescription>
        </CardHeader>
        <CardContent>
          <SignUpForm data={{ email }} />
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
