export type Grade = {
	id: number
	name: string
	factor: number
}

export type Student = {
	id: number
	name: string
	gender: "M" | "F"
	bachelor: boolean
	grades: Array<{
		id: number
		value: number
	}>
}

export const grades: Grade[] = Array.from({ length: 7 }).map((_, index) => ({
	id: index,
	name: "Grade",
	factor: Math.ceil(Math.random() * 3)
}))

export const students: Student[] = Array.from({ length: 30 }).map((_, index) => ({
	id: index,
	name: "John Doe",
	gender: Math.random() > 0.7 ? "F" : "M",
	bachelor: Math.random() > 0.9,
	grades: grades.map(grade => ({
		id: grade.id,
		value: Math.round(Math.random() * 2000) / 100
	}))
}))