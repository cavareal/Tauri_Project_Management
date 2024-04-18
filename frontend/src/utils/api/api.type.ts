import type { Ref, UnwrapRef } from "vue"
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

export type UseQueryResponse<T> = {
	data: Ref<UnwrapRef<T> | null>
	error: Ref<string | null>
	loading: Ref<boolean>
	refetch: () => Promise<void>
}

export type UseMutationResponse = {
	error: Ref<string | null>
	loading: Ref<boolean>
	mutate: () => Promise<void>
}