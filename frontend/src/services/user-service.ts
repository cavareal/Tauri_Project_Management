import type { PermissionType } from "@/types/permission"
import type { RoleType } from "@/types/role"
import { UserSchema, type User } from "@/types/user"
import { queryAndValidate } from "@/utils/api"
import { z } from "zod"

export const getUsersByRole = async(role: RoleType): Promise<User[]> => {
	const response = await queryAndValidate({
		route: `users/roles/${role}`,
		responseSchema: UserSchema.array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const hasPermission = async(user: User, permission: PermissionType): Promise<boolean> => {
	const response = await queryAndValidate({
		route: `users/${user.id}/hasPermission`,
		params: { permissionRequired: permission },
		responseSchema: z.boolean()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}