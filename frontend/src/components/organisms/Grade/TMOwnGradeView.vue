<script setup lang="ts">
import { Table, TableBody, TableCell, TableRow, TableHead, TableHeader } from "@/components/ui/table"
import { cn } from "@/utils/utils"
import getCookie from "@/utils/cookiesUtils"
import { ref } from "vue"

const rowClass = cn("py-2 h-auto")
//const user = getCookie("current_user")
const userId = getCookie("user")
const token = getCookie("token")
const grades = ref()

const request = {
	method: "GET",
	headers: {
		"Content-Type": "application/json",
		Authorization: token || "null"
	}
}

const fetchgrades = async() => {
	try {
		const response = await fetch(import.meta.env.VITE_TAURI_API_URL + "grades/averageGradesByGradeTypeByRole/" + userId, request)
		if (!response.ok) {
			throw new Error(`HTTP error! status: ${response.status}`)
		}
		// eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
		grades.value = await response.json()
	} catch (error) {
		console.error(error)
	}
}
void fetchgrades()

console.log(grades)
</script>

<template>
	<div class="mx-auto flex justify-center">
		<Table>
			<TableHeader>
				<TableRow>
					<TableHead :class="rowClass" class="min-w-64">Matière</TableHead>
					<TableHead :class="rowClass" class="min-w-64">Auteurs</TableHead>
					<TableHead :class="rowClass" class="min-w-64">Note</TableHead>
				</TableRow>
			</TableHeader>
			<TableBody v-for="(row, i) in grades" :key="i">
				<TableRow v-for="(value, j) in row" :key="j">
					<TableCell v-if="i === 0 && value >= 0"><b>Performance Globale</b></TableCell>
					<TableCell v-if="i === 1 && value >= 0"><b>Support Matériel</b></TableCell>
					<TableCell v-if="i === 2 && value >= 0"><b>Contenu de la présentation</b></TableCell>
					<TableCell v-if="j === 4 && value >= 0">TEAM_MEMBER</TableCell>
					<TableCell v-if="j === 0 && value >= 0">SUPERVISING_STAFF</TableCell>
					<TableCell v-if="j === 6 && value >= 0">TECHNICAL_COACH</TableCell>
					<TableCell v-if="value >= 0" :class="rowClass">{{value}}</TableCell>
				</TableRow>
			</TableBody>
		</Table>
	</div>
</template>

<style scoped>

</style>