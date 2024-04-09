<script setup lang="ts">
import { Table, TableBody, TableCell, TableRow, TableHead, TableHeader } from "@/components/ui/table"
import { cn } from "@/utils/utils"
import getCookie from "@/utils/cookiesUtils"
import { ref } from "vue"
import { getAverageGrades } from "@/services/grade-service"

const rowClass = cn("py-2 h-auto")
const user = getCookie("user")
const grades = ref()

if (user != null) {
	grades.value = await getAverageGrades(parseInt(user))
}
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