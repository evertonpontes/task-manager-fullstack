"use client";
import { useAuth } from "@/hooks/use-auth";
import React from "react";

export default function WorkspacePage() {
    const user = useAuth((state) => state.user);

    return <div>Hello</div>;
}
