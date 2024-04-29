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


export type QueryAndValidateRequest<T> = {
	route: string
	params?: Record<string, string>
	jsonContent?: boolean
	delay?: number
	responseSchema: z.ZodType<T>
}

export type QueryAndValidateResponse<T> = {
	status: "success"
	data: T
} | {
	status: "error"
	error: string
}

export type MutateAndValidateRequest<T> = {
	method: "POST" | "PUT" | "PATCH" | "DELETE"
	route: string
	params?: Record<string, string>
	jsonContent?: boolean
	delay?: number
	bodySchema?: z.ZodType<T>
	body?: T
}

export type MutateAndValidateResponse = {
	status: "success"
} | {
	status: "error"
	error: string
}

// export type FetchAndValidateRequest<T> = {
// 	route: string
// 	params?: Record<string, string>
// 	delay?: number
// 	jsonContent?: boolean
// } & ({
// 	method: "GET"
// 	responseSchema: z.ZodType<T>
// } | ({
// 	method: "POST" | "PUT" | "PATCH" | "DELETE"
// 	bodySchema?: z.ZodType<T>
// 	body?: unknown
// }))

// export type FetchAndValidateFunction<T> = ((request: {
// 	route: string
// 	params?: Record<string, string>
// 	delay?: number
// 	jsonContent?: boolean
// 	responseSchema: z.ZodType<T>
// 	method: "GET"
// }) => Promise<QueryAndValidateResponse<T>>) | ((request: {
// 	route: string
// 	params?: Record<string, string>
// 	delay?: number
// 	jsonContent?: boolean
// 	bodySchema?: z.ZodType<T>
// 	body?: unknown
// 	method: "POST" | "PUT" | "PATCH" | "DELETE"
// }) => Promise<MutateAndValidateResponse>)