'use client';

import React from 'react';

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

import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { api, ApiErrorResponse } from '@/lib/api/client';
import { toast } from 'react-toastify';
import axios, { AxiosError } from 'axios';
import { toastConfig } from '@/lib/utils';
import { useRouter } from 'next/navigation';

const formSchema = z.object({
  name: z.string().min(2, {
    message: 'Name must be at least 2 characters',
  }),
  email: z.string().email({
    message: 'Invalid email address',
  }),
  password: z.string().min(8, {
    message: 'Password must be at least 8 characters',
  }),
});

export const RegisterForm = () => {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      name: '',
      email: '',
      password: '',
    },
  });

  const route = useRouter();

  const onSubmit = async (data: z.infer<typeof formSchema>) => {
    try {
      await api.post('/auth/register', data);
      toast.success('Account created successfully', {
        ...toastConfig,
      });

      route.refresh();
      route.push('/sign-in');
    } catch (error) {
      if (axios.isAxiosError(error)) {
        const apiError = error as AxiosError<ApiErrorResponse>;
        const errorResponse = apiError.response?.data.errors[0];

        toast.error(errorResponse, {
          ...toastConfig,
        });
      } else {
        toast.error('Something went wrong', {
          ...toastConfig,
        });
      }
    }
  };

  return (
    <div className="flex flex-col gap-6">
      <Card>
        <CardHeader>
          <CardTitle className="text-2xl">Create an account</CardTitle>
          <CardDescription>
            Enter your email below to create your account
          </CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
              <FormField
                name="name"
                control={form.control}
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Name</FormLabel>
                    <FormControl>
                      <Input placeholder="John Doe" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                name="email"
                control={form.control}
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Email</FormLabel>
                    <FormControl>
                      <Input
                        placeholder="m@example.com"
                        {...field}
                        type="email"
                        autoComplete="username"
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                name="password"
                control={form.control}
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Password</FormLabel>
                    <FormControl>
                      <Input
                        type="password"
                        autoComplete="current-password"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <Button type="submit" className="w-full">
                Register
              </Button>
            </form>
          </Form>
          <div className="mt-4 text-center text-sm">
            Already have an account?{' '}
            <a href="/sign-in" className="underline underline-offset-4">
              Sign in
            </a>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};
