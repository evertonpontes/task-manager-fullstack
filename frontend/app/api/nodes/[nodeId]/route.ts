import { createServer } from "@/lib/api/server";
import { NodeType } from "@/types/node";
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";

export async function POST(
    request: NextRequest,
    { params }: Readonly<{ params: Promise<{ nodeId: string }> }>
) {
    const { nodeId } = await params;
    const cookies = request.cookies;
    const server = await createServer();

    try {
        const accessToken = cookies.get("access-token")?.value;
        const response = await server.post<NodeType>(
            `/api/nodes/${nodeId}`,
            {},
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

export async function PUT(
    request: NextRequest,
    context: Promise<{ params: { nodeId: string } }>
) {
    const params = (await context).params;
    const parentNodeId = params.nodeId;
    const cookies = request.cookies;
    const server = await createServer();

    const body: {
        name: string;
        rank: number;
        parentNodeId: string | null;
    } = await request.json();

    try {
        const accessToken = cookies.get("access-token")?.value;
        const response = await server.put<NodeType>(
            `/api/nodes/${parentNodeId}`,
            body,
            {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${accessToken}`,
                },
                withCredentials: true,
            }
        );

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

export async function DELETE(
    request: NextRequest,
    context: Promise<{ params: { nodeId: string } }>
) {
    const params = (await context).params;
    const parentNodeId = params.nodeId;
    const cookies = request.cookies;
    const server = await createServer();

    try {
        const accessToken = cookies.get("access-token")?.value;
        const response = await server.delete<NodeType>(
            `/api/nodes/${parentNodeId}`,
            {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${accessToken}`,
                },
                withCredentials: true,
            }
        );

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
