import type { PermissionType } from "@/types/permission"
import type { RoleType } from "@/types/role"
import { UserSchema, type User } from "@/types/user"
import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { z } from "zod"

export const getUsersByRole = async(role: RoleType): Promise<User[]> => {
	const response = await queryAndValidate({
		route: `roles/${role}/users`,
		responseSchema: UserSchema.array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const hasPermission = async(userId: number, permission: PermissionType): Promise<boolean> => {
	const response = await queryAndValidate({
		route: `users/${userId}/permissions/${permission}`,
		responseSchema: z.boolean()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const updateUser = async(id: string | null, name: string | null, email: string | null, password: string | null, key: string | null): Promise<void> => {
	const response = await mutateAndValidate({
		method: "PUT",
		route: `users/${id}`,
		body: { name, email, password, key },
		bodySchema: z.any()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const getUserById = async(id: string): Promise<User> => {
	const response = await queryAndValidate({
		route: `users/${id}`,
		responseSchema: UserSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}