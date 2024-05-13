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


export type MutateAndValidateRequestWithReturn<T, R> = {
	method: "POST" | "PUT" | "PATCH" | "DELETE"
	route: string
	params?: Record<string, string>
	jsonContent?: boolean
	delay?: number
	bodySchema?: z.ZodType<T>
	body?: T
	responseSchema: z.ZodType<R>
}

export type MutateAndValidateResponseWithReturn<R> = {
	status: "success"
	data: R
} | {
	status: "error"
	error: string
}