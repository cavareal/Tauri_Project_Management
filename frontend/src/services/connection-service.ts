import { LoginSchema } from "@/types/login"
import { apiQuery } from "@/utils/api"
import { setCookie } from "@/utils/cookie"


export const login = async(username: String, password: String) => {
	
	const response = await apiQuery({
		route: `auth/login`,
		responseSchema: LoginSchema,
		method: "POST",
		body: { username, password}
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
	console.log("response.data : ", response.data)

	setCookie("role", response.data.role.toString())
	setCookie("token", response.data.token.toString())
}