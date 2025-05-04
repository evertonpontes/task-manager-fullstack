'use client';

import React from 'react';
import z from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage,
} from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { toast } from 'react-toastify';
import { toastConfig } from '@/lib/utils';
import { useRouter } from 'next/navigation';
import { useOrganizationModal } from '@/hooks/use-organization-modal';

const formSchema = z.object({
  name: z.string().min(2, {
    message: 'Name is required',
  }),
});

export const OrganizationForm = () => {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      name: '',
    },
  });

  const route = useRouter();
  const { onClose } = useOrganizationModal();

  const onSubmit = async (data: z.infer<typeof formSchema>) => {
    const response = await fetch('/api/organizations', {
      method: 'POST',
      body: JSON.stringify(data),
    });

    if (response.ok) {
      const organization = await response.json();

      route.push(`/workspace/${organization.id}/tasks?view=TableView`);
      route.refresh();
      onClose();
    } else {
      const errorText = await response.text();

      toast.error(errorText, {
        ...toastConfig,
      });
    }
  };

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
        <FormField
          control={form.control}
          name="name"
          render={({ field }) => {
            return (
              <FormItem>
                <FormControl>
                  <Input placeholder="Your organization's name" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            );
          }}
        />
        <Button type="submit">Submit</Button>
      </form>
    </Form>
  );
};
