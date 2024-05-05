import { getAllPermissions, getUsersByRole } from "@/services/user-service"
import type { RoleType } from "@/types/role"
import { Cookies } from "@/utils/cookie"

export const login = async(role: RoleType) => {
	Cookies.setRole(role)
	Cookies.setToken("bonamyRule34")
	Cookies.setProjectId(1)

	const users = await getUsersByRole(role)
	if (users.length === 0) {
		throw new Error("No users found with the given role")
	}
	const user = users[0]
	Cookies.setUserId(user.id)

	const permissions = await getAllPermissions(user.id)
	Cookies.setPermissions(permissions)
}