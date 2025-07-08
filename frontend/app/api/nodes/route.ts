import { createServer } from "@/lib/api/server";
import { NodeType } from "@/types/node";
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";

export async function GET(request: NextRequest) {
    const cookies = request.cookies;
    const server = await createServer();
    try {
        const accessToken = cookies.get("access-token")?.value;
        const response = await server.get<NodeType[]>("/api/nodes", {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
            withCredentials: true,
        });

        return new NextResponse(JSON.stringify(response.data), { status: 200 });
    } catch (error) {
        console.log(error);

        if (error instanceof AxiosError) {
            return new NextResponse(error.response?.data, {
                status: error.response?.status,
            });
        }

        return new NextResponse("Something went wrong", { status: 500 });
    }
}

export async function POST(request: NextRequest) {
    const kind = request.nextUrl.searchParams.get("kind") || "Project";
    const cookies = request.cookies;
    const server = await createServer();

    const body: {
        name: string;
    } = await request.json();

    try {
        const accessToken = cookies.get("access-token")?.value;
        const response = await server.post<NodeType>(
            `/api/nodes?kind=${kind}`,
            body,
            {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${accessToken}`,
                },
                withCredentials: true,
            }
        );

        return new NextResponse(JSON.stringify(response.data), { status: 201 });
    } catch (error) {
        console.log(error);

        if (error instanceof AxiosError) {
            return new NextResponse(error.response?.data, {
                status: error.response?.status,
            });
        }

        return new NextResponse("Something went wrong", { status: 500 });
    }
}
