import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { type Bonus, BonusSchema, type CreateBonus, CreateBonusSchema, type UpdateBonus, UpdateBonusSchema } from "@/types/bonus"
import { getConnectedUser } from "@/services/user-service"
import { z } from "zod"

export const createBonus = async(body: Omit<CreateBonus, "authorId">): Promise<void> => {
	const user = await getConnectedUser()

	const response = await mutateAndValidate({
		method: "POST",
		route: "bonuses",
		body: { ...body, authorId: user.id },
		bodySchema: CreateBonusSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const updateBonus = async(id: string | null, body: UpdateBonus): Promise<void> => {

	const response = await mutateAndValidate({
		method: "PATCH",
		route: `bonuses/${id}`,
		body,
		bodySchema: UpdateBonusSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const getStudentBonuses = async(studentId: number): Promise<Bonus[]> => {
	const response = await queryAndValidate({
		route: `students/${studentId}/bonuses`,
		responseSchema: z.array(BonusSchema)
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	return response.data
}