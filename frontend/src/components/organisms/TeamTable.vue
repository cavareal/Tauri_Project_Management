<script setup lang="ts">
import {
	Table,
	TableBody,
	TableCell,
	TableHead,
	TableHeader,
	TableRow
} from "@/components/ui/table"
import { students } from "@/utils/students"
import TableFooter from "@/components/ui/table/TableFooter.vue"

const getRandomColor = () => {
	const letters = "0123456789ABCDEF"
	let color = "#"
	for (let i = 0; i < 6; i++) {
		color += letters[Math.floor(Math.random() * 16)]
	}
	return color
}

</script>

<template>
	<Table class="w-[400px] h-[200px] overflow-auto rounded-lg bg-gray-100">
		<TableHeader>
			<TableRow class="TableRaw">
				<TableHead>
					Etudiant
				</TableHead>
				<TableHead>
					Role
				</TableHead>
				<TableHead class="text-center">
					Genre
				</TableHead>
			</TableRow>
		</TableHeader>
		<TableBody>
			<TableRow v-for="(student, i) in students.slice(0, 7)" :key="i" class="TableRaw">
				<TableCell>{{ student.name }}</TableCell>
				<TableCell>
					<p :style="{ 'border-radius': '12px', 'background': 'red' }" class="Role">
						{{ student.role }}
					</p>
				</TableCell>
				<TableCell>{{ student.gender }}</TableCell>
			</TableRow>
		</TableBody>
		<TableRow class="TableRaw">
			<TableFooter class="border-none">
				<TableHead>
					Professeur référent :
				</TableHead>
				<TableRow class="TableRaw">
					<TableCell>
						Michael Clavreul
					</TableCell>
				</TableRow>
			</TableFooter>
		</TableRow>
	</Table>
</template>

<style scoped>
.Role {
	display: flex;
	height: 24px;
	justify-content: center;
	align-items: center;
	gap: 10px;
}

.TableRaw {
	border: none;
	height: 10px;
	padding: 0;
}
</style>