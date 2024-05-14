import type { ApiQueryRequest, ApiQueryResponse } from "."
import { Cookies } from "@/utils/cookie"
import type {
	MutateAndValidateRequest, MutateAndValidateRequestWithReturn, MutateAndValidateResponse,
	MutateAndValidateResponseWithReturn, QueryAndValidateRequest, QueryAndValidateResponse
} from "./api.type"
import { wait } from "@/utils/time"
import type { SafeParseReturnType } from "zod"


/**
 * @deprecated Utilisez la fonction newFunction à la place.
 */
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

	const token = Cookies.getToken()

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


// NEW FUNCTIONS

const getApiUrl = () => {
	let url = import.meta.env.VITE_TAURI_API_URL

	if (!url) throw new Error("API URL is not set")
	if (url.endsWith("/")) url = url.slice(0, -1)

	return url
}

const buildUrl = (route: string, params?: Record<string, string>) => {
	if (route.startsWith("/")) route = route.slice(1)

	let url = getApiUrl()
	if (url.endsWith("/")) url = url.slice(0, -1)
	url += `/${route}`
	if (!params) return url

	url += "?"
	for (const key in params) {
		url += `${key}=${params[key]}&`
	}
	url = url.slice(0, -1)

	return url
}

const getHeaders = (jsonContent: boolean = true) => {
	const token = Cookies.getToken()

	const headers = {
		"Authorization": "Bearer " + token || "null"
	}

	if (!jsonContent) return headers

	return {
		...headers,
		"Content-Type": "application/json"
	}
}

/**
 * Fetches data from the API and validates it against a Zod schema.
 * @param route API route to fetch, without the base URL defined in .env (example: "roles" for "{VITE_TAURI_API_URL}/roles")
 * @param params Query parameters to append to the URL (example: { param: value })
 * @param jsonContent Booleam that indicates if the content should be sent as JSON (true by default)
 * @param delay Delay before fetching in ms (useful for testing loading states)
 * @param responseSchema Zod schema to validate the response
 * @returns An object with the status of the request, an error message if it failed and the data if it succeeded
 */
export const queryAndValidate = async <T>({
	route, params, jsonContent = true, delay, responseSchema
}: QueryAndValidateRequest<T>): Promise<QueryAndValidateResponse<T>> => {
	if (delay) await wait(delay)

	const currentProjectId = Cookies.getProjectId()

	const response = await fetch(buildUrl(route, { ...params, projectId: currentProjectId?.toString() ?? "" }), {
		headers: getHeaders(jsonContent)
	})
	if (!response.ok) {
		console.error(`Failed to fetch GET ${route}: ${response.status} ${response.statusText}`)
		return {
			status: "error",
			error: `Fai}led to fetch GET ${route}: ${response.status} ${response.statusText}`
		}
	}

	let data: unknown = await response.text()
	try {
		data = JSON.parse(data as string)
	} catch (error) { /* Do nothing */ }

	const parsedBody = responseSchema.safeParse(data)
	if (!parsedBody.success) {
		console.error(`Failed to validate GET ${route}: ${parsedBody.error.message}`)
		return {
			status: "error",
			error: `Failed to validate GET ${route}: ${parsedBody.error.message}`
		}
	}

	return {
		status: "success",
		data: parsedBody.data
	}
}

/**
 * Mutates data in the API and validates the response.
 * @param method HTTP method to use for mutation (POST, PUT, PATCH or DELETE)
 * @param route API route to fetch, without the base URL defined in .env (example: "roles" for "{VITE_TAURI_API_URL}/roles")
 * @param params Query parameters to append to the URL (example: { param: value })
 * @param jsonContent Booleam that indicates if the content should be sent as JSON (true by default)
 * @param delay Delay before fetching in ms (useful for testing loading states)
 * @param body Data to send in the request body
 * @param bodySchema Zod schema to validate the body
 * @returns An object with the status of the request and an error message if it failed
 */
export const mutateAndValidate = async <T>({
	method, route, params, jsonContent = true, delay, body, bodySchema
}: MutateAndValidateRequest<T>): Promise<MutateAndValidateResponse> => {
	if (delay) await wait(delay)
	if (body && !bodySchema) {
		console.error("Body schema is required when body is provided")
		return {
			status: "error",
			error: "Body schema is required when body is provided"
		}
	}

	let parsedBody: SafeParseReturnType<T, T> | null = null
	if (body && bodySchema) {
		parsedBody = bodySchema.safeParse(body)
		if (!parsedBody.success) {
			console.error(`Failed to validate ${method} ${route}: ${parsedBody.error.message}`)
			return {
				status: "error",
				error: `Failed to validate ${method} ${route}: ${parsedBody.error.message}`
			}
		}
	}

	let bodyData: BodyInit | undefined = undefined
	if (body) bodyData = parsedBody?.data as BodyInit
	if (jsonContent) bodyData = JSON.stringify(bodyData)

	const currentProjectId = Cookies.getProjectId()

	const response = await fetch(buildUrl(route, { ...params, projectId: currentProjectId?.toString() ?? "" }), {
		method,
		body: bodyData,
		headers: getHeaders(jsonContent)
	})
	if (!response.ok) {
		console.error(`Failed to fetch ${method} ${route}: ${response.status} ${response.statusText}`)
		return {
			status: "error",
			error: `Failed to fetch ${method} ${route}: ${response.status} ${response.statusText}`
		}
	}

	return {
		status: "success"
	}
}


/**
 * Mutates data in the API and validates the response.
 * @param method HTTP method to use for mutation (POST, PUT, PATCH or DELETE)
 * @param route API route to fetch, without the base URL defined in .env (example: "roles" for "{VITE_TAURI_API_URL}/roles")
 * @param params Query parameters to append to the URL (example: { param: value })
 * @param jsonContent Booleam that indicates if the content should be sent as JSON (true by default)
 * @param delay Delay before fetching in ms (useful for testing loading states)
 * @param body Data to send in the request body
 * @param bodySchema Zod schema to validate the body
 * @param responseSchema Zod schema to validate the response
 * @returns An object with the status of the request and an error message if it failed
 */
export const mutateAndValidateWithReturn = async <T, R>({
	method, route, params, jsonContent = true, delay, body, bodySchema, responseSchema
}: MutateAndValidateRequestWithReturn<T, R>): Promise<MutateAndValidateResponseWithReturn<R>> => {
	if (delay) await wait(delay)
	if (body && !bodySchema) return {
		status: "error",
		error: "Body schema is required when body is provided"
	}

	let parsedBody: SafeParseReturnType<T, T> | null = null
	if (body && bodySchema) {
		parsedBody = bodySchema.safeParse(body)
		if (!parsedBody.success) return {
			status: "error",
			error: `Failed to validate ${method} ${route}: ${parsedBody.error.message}`
		}
	}

	let bodyData: BodyInit | undefined = undefined
	if (body) bodyData = parsedBody?.data as BodyInit
	if (jsonContent) bodyData = JSON.stringify(bodyData)

	const currentProjectId = Cookies.getProjectId()

	const response = await fetch(buildUrl(route, { ...params, projectId: currentProjectId?.toString() ?? "" }), {
		method,
		body: bodyData,
		headers: { "Content-Type": "application/json" }
	})
	if (!response.ok) return {
		status: "error",
		error: `Failed to fetch ${method} ${route}: ${response.status} ${response.statusText}`
	}

	let data: unknown = await response.text()
	try {
		data = JSON.parse(data as string)
	} catch (error) { /* Do nothing */ }

	const parsedResponse = responseSchema.safeParse(data)
	if (!parsedResponse.success) return {
		status: "error",
		error: `Failed to validate GET ${route}: ${parsedResponse.error.message}`
	}

	return {
		status: "success",
		data: parsedResponse.data
	}
}