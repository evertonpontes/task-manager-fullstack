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
            try {
                await axios.post(
                    "/api/auth/refresh-token",
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
        }, 1000 * 60);

        return () => clearInterval(refreshInterval);
    }, [accessToken, setToken]);
}
