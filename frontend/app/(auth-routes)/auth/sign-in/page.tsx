import { SignInForm } from '@/components/auth/sign-in-form';
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

export default function SignInPage() {
  return (
    <div className="flex flex-col gap-6">
      <Card>
        <CardHeader>
          <CardTitle className="text-2xl">Welcome to Task Manager</CardTitle>
          <CardDescription>Get started by creating a account</CardDescription>
        </CardHeader>
        <CardContent>
          <SignInForm />
        </CardContent>
        <CardFooter>
          <div className="text-center text-sm">
            Don&apos;t have an account?{' '}
            <Link href={'/auth/start'} className="underline underline-offset-2">
              Sign up
            </Link>
          </div>
        </CardFooter>
      </Card>
    </div>
  );
}
