"use client";
import React from "react";
import {
    Dialog,
    DialogClose,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from "./dialog";
import { Button } from "./button";

interface ModalProps {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    children: React.ReactNode;
    title: string;
    action: () => void;
    actionText: string;
    actionType?: "default" | "destructive";
}

export const Modal: React.FC<ModalProps> = ({
    open,
    onOpenChange,
    children,
    title,
    action,
    actionText,
    actionType = "default",
}) => {
    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent className="sm:max-w-[450px]">
                <DialogHeader>
                    <DialogTitle>{title}</DialogTitle>
                    <DialogDescription className="sr-only">
                        Description goes here.
                    </DialogDescription>
                </DialogHeader>
                <div>{children}</div>
                <DialogFooter>
                    <DialogClose asChild>
                        <Button variant="outline">Cancel</Button>
                    </DialogClose>
                    <Button type="submit" variant={actionType} onClick={action}>
                        {actionText}
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
};
