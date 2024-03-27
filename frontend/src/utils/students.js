export const grades = Array.from({ length: 7 }).map(index => ({
	id: index,
	name: "Grade",
	factor: Math.ceil(Math.random() * 3)
}))

export const students = Array.from({ length: 30 }).map(index => ({
	id: index,
	name: "John Doe",
	gender: Math.random() > 0.7 ? "F" : "M",
	bachelor: Math.random() > 0.9,
	grades: grades.map(grade => ({
		id: grade.id,
		value: Math.round(Math.random() * 2000) / 100
	}))
}))