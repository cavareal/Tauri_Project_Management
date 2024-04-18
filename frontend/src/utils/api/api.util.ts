import { ref, watch, type UnwrapRef } from "vue"
import type { ApiQueryRequest, ApiQueryResponse } from "."
import { getCookie } from "@/utils/cookie"
import type { UploadFileRequest, UseMutationResponse, UseQueryResponse } from "./api.type"


export const apiQuery = async <T>(
	{ route, responseSchema, method, body, delay = 0, textResponse = false }: ApiQueryRequest<T>
): Promise<ApiQueryResponse<T>> => {
	let url = import.meta.env.VITE_TAURI_API_URL
	if (!url) {
		return {
			status: "error",
			error: "API URL is not set"
		}
	}
	if (!url.endsWith("/")) url += "/"
	if (route.startsWith("/")) route = route.slice(1)

	if (delay) await new Promise(resolve => setTimeout(resolve, delay))

	const token = getCookie("token")

	const headers = {
		"Authorization": token || "null",
		"Content-Type": "application/json"
	}

	const response = await fetch(`${url}${route}`, { method, body: body ? JSON.stringify(body) : undefined, headers })
	if (!response.ok) {
		return {
			status: "error",
			error: `Failed to fetch ${url}${route}`
		}
	}

	let data = null

	if (textResponse) {
		const textData = await response.text()
		data = responseSchema.safeParse(textData)
	} else {
		const jsonData: unknown = await response.json()
		data = responseSchema.safeParse(jsonData)
	}

	if (!data.success) {
		return {
			status: "error",
			error: `Failed to parse ${route}: ${data.error.message}`
		}
	}

	return {
		status: "success",
		data: data.data
	}
}

export const uploadFile = async({ file, route }: UploadFileRequest): Promise<ApiQueryResponse<string>> => {
	let url = import.meta.env.VITE_TAURI_API_URL
	if (!url) {
		return {
			status: "error",
			error: "API URL is not set"
		}
	}
	if (!url.endsWith("/")) url += "/"
	if (route.startsWith("/")) route = route.slice(1)

	const token = getCookie("token")

	const headers = {
		"Authorization": token || "null"
	}

	const formData = new FormData()
	formData.append("file-upload", file)
	const response = await fetch(`${url}${route}`, { method: "POST", body: formData, headers })
	if (!response.ok) {
		return {
			status: "error",
			error: `Failed to fetch ${url}${route}`
		}
	}

	return {
		status: "success",
		data: await response.text()
	}
}

export const useQuery = <T>(queryFunction: () => Promise<T>): UseQueryResponse<T> => {
	const data = ref<T | null>(null)
	const error = ref<string | null>(null)
	const loading = ref(false)

	const fetchData = async() => {
		loading.value = true
		await queryFunction()
			.then((res: T) => {
				data.value = res as UnwrapRef<T>
				error.value = null
			})
			.catch((e: Error) => {
				error.value = e?.message ?? "Unknown error"
			})
			.finally(() => {
				loading.value = false
			})
	}

	watch(() => {}, fetchData, { immediate: true })

	return { data, error, loading, refetch: fetchData }
}

export const useMutation = (mutationFunction: () => Promise<void>): UseMutationResponse => {
	const error = ref<string | null>(null)
	const loading = ref(false)

	const mutate = async() => {
		loading.value = true
		await mutationFunction()
			.then(() => {
				error.value = null
			})
			.catch((e: Error) => {
				error.value = e?.message ?? "Unknown error"
			})
			.finally(() => {
				loading.value = false
			})
	}

	return { error, loading, mutate }
}