import { AuthResponseSchema } from "@/types/auth-response"
import { apiQuery, mutateAndValidateWithReturn } from "@/utils/api"
import { Cookies } from "@/utils/cookie"
import { getAllPermissions, getAllRoles } from "@/services/user-service"
import { AuthRequestSchema } from "@/types/auth-request"

export const login = async(login: string, password: string) => {
	// TODO: set the project id
	Cookies.setProjectId(1)

	console.log("Login : " + JSON.stringify({ login, password }))

	const response = await mutateAndValidateWithReturn({
		route: "auth/login",
		responseSchema: AuthResponseSchema,
		bodySchema: AuthRequestSchema,
		method: "POST",
		body: { login, password }
	})

	console.log("response: ", response)

	if (response.status === "error") {
		throw new Error(response.error)
	}

	Cookies.setUserId(response.data.id)
	Cookies.setToken(response.data.accessToken)


	const roles = await getAllRoles(response.data.id)
	// TODO: set all roles and not only the first one
	Cookies.setRole(roles[0])

	const permissions = await getAllPermissions(response.data.id)
	Cookies.setPermissions(permissions)
}