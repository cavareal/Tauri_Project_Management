import { GradeTypeSchema, UpdateGradeTypeSchema, type UpdateGradeType } from "@/types/grade-type"
import { mutateAndValidate, queryAndValidate } from "@/utils/api"
import { z } from "zod"
import type { GradeType } from "@/types/grade-type"

export const getAllImportedGradeTypes = async() => {
	const response = await queryAndValidate({
		responseSchema: z.array(GradeTypeSchema),
		route: "grade-types/imported"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}

	const averageIndex = response.data.findIndex(gradeType => gradeType.name === "Moyenne")
	if (averageIndex !== -1) {
		const average = response.data[averageIndex]
		response.data.splice(averageIndex, 1)
		response.data.unshift(average)
	}

	return response.data.filter(gradeType => gradeType.imported)
}

export const updateGradeType = async(id: number, body: UpdateGradeType): Promise<void> => {
	const response = await mutateAndValidate({
		method: "PATCH",
		route: `grade-types/${id}`,
		body,
		bodySchema: UpdateGradeTypeSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
}

export const getGradeTypeByName = async(name: string): Promise<GradeType> => {
	const response = await queryAndValidate({
		route: "grade-types/name",
		params: { name: name },
		responseSchema: GradeTypeSchema
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
	return response.data
}


export const downloadGradeScalePDF = async(gradeTypeId: number): Promise<void> => {
	const response = await queryAndValidate({
		route: `grade-types/${gradeTypeId}/download-pdf`,
		responseSchema: z.string()
	})

	if (response.status === "error") {
		throw new Error(response.error)
	}
	console.log(new Blob([response.data]))
	const url = window.URL.createObjectURL(new Blob([response.data]))
	const link = document.createElement("a")
	link.href = url
	link.setAttribute("download", "gradescale.pdf")
	document.body.appendChild(link)
	link.click()
	document.body.removeChild(link)
}