'use client';

import React, { useState } from 'react';
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from '@/components/ui/collapsible';
import { ChevronDown, Plus } from 'lucide-react';
import { cn } from '@/lib/utils';

type CollapsibleItemProps = {
  text: string;
  children: React.ReactNode;
  onClick?: () => void;
};

export const CollapsibleItem: React.FC<CollapsibleItemProps> = ({
  text,
  children,
  onClick,
}) => {
  const [open, setOpen] = useState(true);

  const handleOpen = () => {
    setOpen(!open);
  };

  return (
    <Collapsible onOpenChange={handleOpen} open={open}>
      <div className="flex w-full justify-between items-center gap-2 whitespace-nowrap rounded-md font-medium transition-all px-4 py-2 has-[>svg]:px-3 hover:bg-accent hover:text-accent-foreground dark:hover:bg-accent/50">
        <CollapsibleTrigger className="w-full flex items-center gap-2 cursor-pointer">
          <ChevronDown
            className={cn(
              'mr-2 h-4 w-4 transition-transform',
              open ? '' : 'rotate-180'
            )}
          />
          <span>{text}</span>
        </CollapsibleTrigger>
        <button className="cursor-pointer" onClick={onClick}>
          <Plus className="h-4 w-4" />
        </button>
      </div>
      <CollapsibleContent>{children}</CollapsibleContent>
    </Collapsible>
  );
};
