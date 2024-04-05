<script setup lang="ts">
import {
	Table,
	TableBody,
	TableCell,
	TableHead,
	TableHeader,
	TableRow
} from "@/components/ui/table"
import TableFooter from "@/components/ui/table/TableFooter.vue"
import type { Student } from "@/types/student"
import { ref, onMounted } from "vue"
import { getStudentsByTeamId } from "@/services/student-service"

const students = ref<Student[]>([])

const props = defineProps({
	teamId: {
		type: Number,
		required: true
	}
})

onMounted(async() => {
	const data = await getStudentsByTeamId(props.teamId)
	students.value = data
})

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
			<TableRow v-for="(student, i) in students" :key="i" class="TableRaw">
				<TableCell>{{ student.name }}</TableCell>
				<TableCell>
					<p :style="{ 'border-radius': '12px', 'background': 'red' }" class="Role">
						{{ student.teamRole }}
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