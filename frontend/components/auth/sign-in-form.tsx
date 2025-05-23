'use client';
import React, { useTransition } from 'react';
import { z } from 'zod';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { GithubIcon, GoogleIcon } from '@/components/icons';
import { useRouter } from 'next/navigation';
import { useAuth, UserResponse } from '@/hooks/useAuth';
import axios, { AxiosError } from 'axios';
import { ErrorResponse } from '@/data/response';
import { toast } from 'react-toastify';
import Link from 'next/link';

const formSchema = z.object({
  email: z.string().email(),
  password: z.string().min(8),
});

export const SignInForm = () => {
  const [isPending, startTransition] = useTransition();

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: '',
      password: '',
    },
  });

  const router = useRouter();
  const setUser = useAuth((state) => state.setUser);

  const onSubmit = async (values: z.infer<typeof formSchema>) => {
    startTransition(async () => {
      try {
        const response = await axios.post<UserResponse>(
          `${process.env.NEXT_PUBLIC_API_URL}/api/auth/login`,
          values,
          {
            withCredentials: true,
          }
        );

        setUser(response.data);
        router.push('/workspace');
      } catch (error) {
        if (error instanceof Error) {
          console.log(error.message);
        }
        if (error instanceof AxiosError) {
          const errorResponse = error as AxiosError<ErrorResponse>;
          const message = errorResponse.response?.data.errors[0];

          toast.error(message);
        }
      }
    });
  };

  const oauth2LoginLink = (provider: 'google' | 'github') => {
    return (
      process.env.NEXT_PUBLIC_API_URL + `/oauth2/authorization/${provider}`
    );
  };

  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className="flex flex-col space-y-6"
      >
        <Link href={oauth2LoginLink('google')}>
          <Button type="button" variant="outline" className="w-full">
            <GoogleIcon />
            Login with Google
          </Button>
        </Link>
        <Link href={oauth2LoginLink('github')}>
          <Button type="button" variant="outline" className="w-full">
            <GithubIcon />
            Login with Github
          </Button>
        </Link>
        <div className="relative text-center text-sm after:absolute after:inset-0 after:top-1/2 after:z-0 after:flex after:items-center after:border-t after:border-border">
          <span className="relative z-10 bg-background px-2 text-muted-foreground">
            Or continue with
          </span>
        </div>
        <FormField
          control={form.control}
          name="email"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Email</FormLabel>
              <FormControl>
                <Input
                  type="email"
                  autoComplete="email"
                  placeholder="m@example.com"
                  {...field}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="password"
          render={({ field }) => (
            <FormItem>
              <div className="flex items-center">
                <FormLabel>Password</FormLabel>
                <Link
                  href={'/auth/forgot-password'}
                  className="ml-auto inline-block text-sm underline-offset-4 hover:underline"
                >
                  Forgot your password?
                </Link>
              </div>
              <FormControl>
                <Input
                  type="password"
                  autoComplete="password"
                  placeholder=""
                  {...field}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <Button type="submit" className="w-full" disabled={isPending}>
          {isPending ? 'Loading...' : 'Log in'}
        </Button>
      </form>
    </Form>
  );
};
