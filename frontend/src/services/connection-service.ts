import type { RoleType } from "@/types/role"
import { LoginSchema } from "@/types/login"
import { UserSchema } from "@/types/user"
import { apiQuery } from "@/utils/api"
import { setCookie } from "@/utils/cookie"

export const login = async(role: RoleType) => {
	setCookie("role", role)
	setCookie("token", "bonamyRule34")

	const response = await apiQuery({
		route: `users/roles/${role}`,
		responseSchema: UserSchema.array(),
		method: "GET"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	setCookie("user", response.data[0].id.toString())
}

export const log = async(username: String, password: String) => {
	
	const response = await apiQuery({
		route: `api/auth/login`,
		responseSchema: LoginSchema,
		method: "POST"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	setCookie("role", response.data[0].role.toString())
	setCookie("token", response.data[0].token.toString())

}