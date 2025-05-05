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
import { useParams } from 'next/navigation';
import { useTeamModal } from '@/hooks/use-team-modal';
import { useTeam } from '@/hooks/use-team';

const formSchema = z.object({
  name: z.string().min(2, {
    message: 'Name is required',
  }),
});

export const TeamForm = () => {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      name: '',
    },
  });

  const { onClose } = useTeamModal();
  const params = useParams<{ organizationId: string }>();
  const organizationId = params?.organizationId;
  const teams = useTeam((state) => state.teams);
  const setTeams = useTeam((state) => state.setTeams);

  const onSubmit = async (data: z.infer<typeof formSchema>) => {
    const response = await fetch(`/api/organizations/${organizationId}/teams`, {
      method: 'POST',
      body: JSON.stringify(data),
    });

    if (response.ok) {
      toast.success('The new team has been successfully created', {
        ...toastConfig,
      });

      const team = await response.json();
      setTeams([...teams, team]);

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
                  <Input placeholder="Team name" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            );
          }}
        />
        <Button
          type="button"
          variant="ghost"
          onClick={onClose}
          className="mr-2"
        >
          Cancel
        </Button>
        <Button type="submit">Create team</Button>
      </form>
    </Form>
  );
};
