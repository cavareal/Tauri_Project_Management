import { AuthResponseSchema } from "@/types/auth-response"
import { apiQuery } from "@/utils/api"
import { setCookie } from "@/utils/cookie"
import { z } from "zod"


export const login = async(login: string, password: string) => {
	console.log("Login : " + JSON.stringify({ login, password }))

	const response = await apiQuery({
		route: "auth/login",
		responseSchema: z.any(),
		method: "POST",
		body: { login, password }
	})

	console.log("login", "1")

	console.log("response: ", response)

	console.log("login", "2")

	if (response.status === "error") {
		throw new Error(response.error)
	}
	const user = users[0]
	Cookies.setUserId(user.id)

	console.log("login", "3")

	console.log("response.data : ", response.data)

	console.log("login", "4")

//	setCookie("role", response.data.login.toString())
//	setCookie("token", response.data.accessToken.toString())
}