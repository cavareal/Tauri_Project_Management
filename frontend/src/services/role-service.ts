import type { PermissionType } from "@/types/permission"
import type { RoleType } from "@/types/role"
import { queryAndValidate } from "@/utils/api"
import { z } from "zod"

export const hasPermission = async(roleType: RoleType, permission: PermissionType): Promise<boolean> => {
	const response = await queryAndValidate({
		route: `roles/${roleType}/permissions/${permission}`,
		responseSchema: z.boolean()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}