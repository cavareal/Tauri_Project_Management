import { PermissionTypeSchema, type PermissionType } from "@/types/permission"
import type { RoleType } from "@/types/role"
import { UserSchema, type UpdateUser, type User } from "@/types/user"
import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { Cookies } from "@/utils/cookie"
import { z } from "zod"
import { RoleTypeSchema } from "@/types/role"

export const getConnectedUser = async(): Promise<User> => {
	const userId = Cookies.getUserId()

	const response = await queryAndValidate({
		route: `users/${userId}`,
		responseSchema: UserSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

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

export const updateUser = async(id: string | null, body: UpdateUser): Promise<void> => {
	const response = await mutateAndValidate({
		method: "PUT",
		route: `users/${id}`,
		body,
		bodySchema: z.any()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const getUserById = async(id: number): Promise<User> => {
	const response = await queryAndValidate({
		route: `users/${id}`,
		responseSchema: UserSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const getAllPermissions = async(id: number): Promise<PermissionType[]> => {
	const response = await queryAndValidate({
		route: `users/${id}/permissions`,
		responseSchema: PermissionTypeSchema.array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}

export const hasPermission = (permission: PermissionType): boolean => {
	const permissions = Cookies.getPermissions()

	return permissions.includes(permission)
}

export const getCurrentUser = async(): Promise<User> => {
	const userId = Cookies.getUserId()

	return await getUserById(userId)
}

export const getAllRoles = async(id: number): Promise<RoleType[]> => {
	const response = await queryAndValidate({
		route: `users/${id}/roles`,
		responseSchema: RoleTypeSchema.array()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}


export const createUser = async(email: string, name: string, role: RoleType): Promise<void> => {
	console.log("ouai la tem request")
	const response = await mutateAndValidate({
		method: "POST",
		body: { email, name, role },
		route: `users`,
		bodySchema: z.unknown(),
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

