import { createServer } from "@/lib/api/server";
import { decodeJwt } from "@/lib/utils";
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";

export async function POST(request: NextRequest) {
    const cookies = request.cookies;
    const server = await createServer();

    try {
        const accessToken = cookies.get("access-token")?.value || "";

        const decoded = decodeJwt(accessToken);

        if (!decoded) throw new Error("Invalid token");

        const now = Date.now() / 1000;
        const timeLeft = decoded.exp - now;

        if (Math.floor(timeLeft) < 60) {
            const response = await server.post(
                "/api/auth/refresh-token",
                {},
                {
                    headers: {
                        Cookie: cookies.toString(),
                    },
                }
            );

            return new NextResponse("", {
                status: 200,
                headers: Object.entries(response.headers),
            });
        }

        return new NextResponse("", {
            status: 200,
            headers: Object.entries(cookies),
        });
    } catch (error) {
        console.log("REFRESH-TOKEN ERROR", error);

        if (error instanceof AxiosError) {
            return new NextResponse(error.response?.data, {
                status: error.response?.status,
            });
        }

        return new NextResponse("Something went wrong", { status: 500 });
    }
}
