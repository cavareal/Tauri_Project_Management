import type { RoleType } from "@/types/role"
import { UserSchema } from "@/types/user"
import { queryAndValidate } from "@/utils/api"
import { setCookie } from "@/utils/cookie"

export const login = async(role: RoleType) => {
	setCookie("role", role)
	setCookie("token", "bonamyRule34")
	setCookie("currentProject", "1")

	const response = await queryAndValidate({
		route: `roles/${role}/users`,
		responseSchema: UserSchema.array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	setCookie("user", response.data[0].id.toString())
	console.log(response.data)
}