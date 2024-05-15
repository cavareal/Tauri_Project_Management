import { AuthResponseSchema } from "@/types/auth-response"
import { loginAndValidate } from "@/utils/api"
import { Cookies } from "@/utils/cookie"
import { getAllPermissions, getAllRoles } from "@/services/user-service"
import { AuthRequestSchema } from "@/types/auth-request"

export const login = async(login: string, password: string) => {
	Cookies.removeAll()
	// TODO: set the project id
	Cookies.setProjectId(1)

	const response = await loginAndValidate({
		route: "auth/login",
		responseSchema: AuthResponseSchema,
		bodySchema: AuthRequestSchema,
		body: { login, password }
	})

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