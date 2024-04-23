import type { z } from "zod"

export type ApiQueryRequest<T> = {
	route: string
	method: "GET" | "POST" | "PUT" | "PATCH" | "DELETE"
	body?: unknown
	responseSchema: z.ZodType<T>
	delay?: number
	textResponse?: boolean
}

export type ApiQueryResponse<T> = {
    status: "success"
    data: T
} | {
    status: "error"
    error: string
}

export type UploadFileRequest = {
	file: File
	route: string
}