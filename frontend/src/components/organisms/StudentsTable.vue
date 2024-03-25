<script>
import Table from "@/components/ui/table/Table.vue"
import TableCell from "@/components/ui/table/TableCell.vue"
import TableRow from "@/components/ui/table/TableRow.vue"
import TableBody from "@/components/ui/table/TableBody.vue"
import TableHead from "@/components/ui/table/TableHead.vue"
import TableHeader from "@/components/ui/table/TableHeader.vue"

// console.log(students)

export default {
	name: "StudentsTable",
	components: { Table, TableHeader, TableHead, TableBody, TableRow, TableCell },
	data() {
		const grades = Array.from({ length: 7 }).map(index => ({
			id: index,
			name: "Grade",
			factor: Math.ceil(Math.random() * 3)
		}))

		const students = Array.from({ length: 30 }).map(index => ({
			id: index,
			name: "John Doe",
			gender: Math.random() > 0.7 ? "F" : "M",
			bachelor: Math.random() > 0.9,
			grades: grades.map(grade => ({
				id: grade.id,
				grade: Math.round(Math.random() * 2000) / 100
			}))
		}))

		return {
			grades,
			students // Initialiser la liste d'étudiants vide
		}
	}
}
</script>

<template>
	<Table>
		<TableHeader>
			<TableRow class="flex flex-row justify-between items-center">
				<TableHead class="flex flex-col items-center justify-center">Nom</TableHead>
				<TableHead class="flex flex-col items-center justify-center">Genre</TableHead>
				<TableHead class="flex flex-col items-center justify-center">Bachelor</TableHead>
				<TableHead v-for="grade in grades" :key="grade.id" class="flex flex-col justify-center items-center">
					{{ grade.name }}
					<p class="text-xs text-gray-400">Coefficient {{ grade.factor }}</p>
				</TableHead>
			</TableRow>
		</TableHeader>
		<TableBody>
			<TableRow v-for="student in students" :key="student.id">
				<TableCell class="font-medium">
					{{ student.name }}
				</TableCell>
				<TableCell>{{ student.gender }}</TableCell>
				<TableCell>{{ student.bachelor ? "Oui" : "Non" }}</TableCell>
				<TableCell class="text-right">
					{{ }}
				</TableCell>
			</TableRow>
		</TableBody>
	</Table>
</template>

<style scoped>

</style>