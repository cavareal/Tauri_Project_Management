import type { ApiQueryRequest, ApiQueryResponse } from "."
import getCookie from "@/utils/cookiesUtils"


export const apiQuery = async <T>({ route, responseSchema, method, body }: ApiQueryRequest<T>): Promise<ApiQueryResponse<T>> => {
	let url = import.meta.env.VITE_TAURI_API_URL
	if (!url) {
		return {
			status: "error",
			error: "API URL is not set"
		}
	}
	if (!url.endsWith("/")) url += "/"

	const token = getCookie("token")

	const headers = {
		"Content-Type": "application/json",
		Authorization: token || "null"
	}

	const response = await fetch(`${url}${route}`, { method, body: body ? JSON.stringify(body) : undefined, headers })
	if (!response.ok) {
		return {
			status: "error",
			error: `Failed to fetch ${url}${route}`
		}
	}

	const data = responseSchema.safeParse(await response.json())
	if (!data.success) {
		return {
			status: "error",
			error: `Failed to parse ${route}`
		}
	}

	return {
		status: "success",
		data: data.data
	}
}