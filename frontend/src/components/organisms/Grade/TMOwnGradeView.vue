<script setup lang="ts">
import { Table, TableBody, TableCell, TableRow, TableHead, TableHeader } from "@/components/ui/table"
import { cn } from "@/utils/utils"
import getCookie from "@/utils/cookiesUtils"
import { ref } from "vue"

const rowClass = cn("py-2 h-auto")
const token = getCookie("token")
const user = getCookie("user")
const grades = ref([])

const fetchGrades = async() => {
	try {
		const response = await fetch(import.meta.env.VITE_TAURI_API_URL + "grades/averageGradesByGradeTypeByRole", {
			method: "GET",
			headers: {
				"Content-Type": "application/json",
				Authorization: token || "null",
				User: user || "null"
			}
		})

		if (!response.ok) {
			throw new Error(`HTTP error! status: ${response.status}`)
		}
		// eslint-disable-next-line @typescript-eslint/no-unsafe-return,@typescript-eslint/no-unsafe-assignment
		grades.value = await response.json()
	} catch (error) {
		console.error(error)
	}
}

void fetchGrades()

</script>

<template>
	<Table>
		<TableHeader>
			<TableRow>
				<TableHead :class="rowClass" class="min-w-64" >Matière</TableHead>
				<TableHead :class="rowClass" class="min-w-64">Note</TableHead>
			</TableRow>
		</TableHeader>
		<TableBody>
			<TableRow>
				<TableCell :class="rowClass">{{}}</TableCell>
				<TableCell :class="rowClass">{{}}</TableCell>
			</TableRow>
		</TableBody>
	</Table>
</template>

<style scoped>

</style>