import { decodeJwt } from "@/lib/utils";
import axios from "axios";
import { useEffect } from "react";
import { create } from "zustand";

export type UserResponse = {
    id: string;
    firstName: string;
    lastName: string;
    email: string;
    role: string;
    picture: string;
    isEmailVerified: boolean;
    createdAt: Date;
    updatedAt: Date;
};

type AuthProps = {
    user?: UserResponse;
    token?: string;
    setUser: (user: UserResponse) => void;
    setToken: (token: string) => void;
};

export const useAuth = create<AuthProps>((set) => ({
    user: undefined,
    token: undefined,
    setUser: (user) => set({ user }),
    setToken: (token) => set({ token }),
}));

export function useAutoRefreshToken(accessToken: string | undefined) {
    const setToken = useAuth((state) => state.setToken);

    useEffect(() => {
        const refreshInterval = setInterval(async () => {
            if (!accessToken) return null;

            const decoded = decodeJwt(accessToken);

            if (!decoded) return null;

            const now = Date.now() / 1000;
            const timeLeft = decoded.exp - now;

            if (timeLeft < 60) {
                try {
                    await axios.post(
                        `${process.env.NEXT_PUBLIC_API_URL}/api/auth/refresh-token`,
                        {},
                        {
                            withCredentials: true,
                        }
                    );
                } catch (error) {
                    if (error instanceof Error) {
                        console.log(error.message);
                    }
                }
            }
        }, 1000 * 60);

        return () => clearInterval(refreshInterval);
    }, [accessToken]);
}
