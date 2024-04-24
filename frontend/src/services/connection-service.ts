import type { RoleType } from "@/types/role"
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