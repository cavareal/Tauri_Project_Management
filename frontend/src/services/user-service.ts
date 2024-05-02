import type { PermissionType } from "@/types/permission"
import type { RoleType } from "@/types/role"
import { UserSchema, type User } from "@/types/user"
import { apiQuery } from "@/utils/api"
import { z } from "zod"

export const getUsersByRole = async(role: RoleType): Promise<User[]> => {
	const response = await apiQuery({
		route: `roles/${role}/users`,
		responseSchema: z.array(UserSchema),
		method: "GET"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const hasPermission = async(user: User, permission: PermissionType): Promise<boolean> => {
	const response = await apiQuery({
		route: `users/${user.id}/permissions/${permission}`,
		responseSchema: z.boolean(),
		method: "GET"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const updateUser = async(id: string | null, name: string | null, email: string | null, password: string | null, key: string | null): Promise<void> => {
	const response = await apiQuery({
		method: "PUT",
		route: `users/${id}`,
		body: { name, email, password, key },
		responseSchema: z.string(),
		textResponse: true
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}