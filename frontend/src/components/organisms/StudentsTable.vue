<script setup lang="ts">
import { Table, TableBody, TableCell, TableRow, TableHead, TableHeader } from "@/components/ui/table"
import { grades, students } from "@/utils/students"
</script>

<template>
	<Table>
		<TableHeader>
			<TableRow>
				<TableHead>Nom</TableHead>
				<TableHead>Genre</TableHead>
				<TableHead>Bachelor</TableHead>
				<TableHead v-for="(grade, i) in grades" :key="i">
					<div class="flex flex-col items-start justify-center">
						{{ grade.name }}
						<p class="text-xs text-gray-400 font-normal">Coefficient {{ grade.factor }}</p>
					</div>
				</TableHead>
			</TableRow>
		</TableHeader>
		<TableBody>
			<TableRow v-for="(student, i) in students" :key="i">
				<TableCell class="font-medium">
					{{ student.name }}
				</TableCell>
				<TableCell>{{ student.gender }}</TableCell>
				<TableCell>{{ student.bachelor ? "Oui" : "Non" }}</TableCell>
				<TableCell v-for="grade in grades" :key="grade.id">
					{{ student.grades.find(g => g.id === grade.id)?.value ?? "" }}
				</TableCell>
			</TableRow>
		</TableBody>
	</Table>
</template>