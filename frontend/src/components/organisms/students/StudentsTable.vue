<script setup lang="ts">
import { Table, TableBody, TableCell, TableRow, TableHead, TableHeader } from "@/components/ui/table"
import { Skeleton } from "@/components/ui/skeleton"
import { cn } from "@/utils/utils"
import type { Student } from "@/types/student"
import { ref, watch } from "vue"
import type { GradeType } from "@/types/grade-type"
import type { Grade } from "@/types/grade"
import { getAllStudents } from "@/services/student-service"
import { getAllGrades } from "@/services/grade-service"
import { getAllImportedGradeTypes } from "@/services/grade-type-service"
import { Check, X } from "lucide-vue-next"

const rowClass = cn("py-2 h-auto")

const gradeTypes = ref<GradeType[] | null>(null)
const students = ref<Student[] | null>(null)
const grades = ref<Grade[] | null>(null)

watch(() => { }, async() => {
	gradeTypes.value = await getAllImportedGradeTypes()
}, { immediate: true })

watch(() => { }, async() => {
	students.value = await getAllStudents()
}, { immediate: true })

watch(() => { }, async() => {
	grades.value = await getAllGrades()
}, { immediate: true })

</script>

<template>
	<Table v-if="gradeTypes">
		<TableHeader>
			<TableRow>
				<TableHead :class="rowClass" class="min-w-64">Nom</TableHead>
				<TableHead :class="rowClass" class="min-w-28">Genre</TableHead>
				<TableHead :class="rowClass" class="min-w-28">Bachelor</TableHead>
				<TableHead v-for="(gradeType, i) in gradeTypes" :key="i" :class="rowClass" class="min-w-32">
					<span v-if="['mean', 'average'].includes(gradeType.name.toLowerCase())">Moyenne</span>
					<span v-else>{{ gradeType.name }} ({{ gradeType.factor }})</span>
				</TableHead>
			</TableRow>
		</TableHeader>
		<TableBody v-if="students">
			<TableRow v-for="(student, i) in students" :key="i">
				<TableCell class="font-medium" :class="rowClass">
					{{ student.name }}
				</TableCell>
				<TableCell :class="rowClass">{{ student.gender === "MAN" ? "H" : "F" }}</TableCell>
				<TableCell :class="rowClass">
					<Check v-if="student.bachelor" class="w-4"/>
					<X v-else class="w-4"/>
				</TableCell>
				<TableCell v-for="(gradeType, i) in gradeTypes" :key="i" :class="rowClass">
					<Skeleton v-if="!grades" class="w-5/6 h-5" />
					<span v-else>
						{{ grades?.find(grade => grade.student?.id === student.id && grade.gradeType.id === gradeType.id)?.value ?? "" }}
					</span>
				</TableCell>
			</TableRow>
		</TableBody>
		<TableBody v-else>
			<TableRow v-for="i in 10" :key="i">
				<TableCell :class="rowClass">
					<Skeleton class="w-5/6 h-5" />
				</TableCell>
				<TableCell :class="rowClass">
					<Skeleton class="w-5/6 h-5" />
				</TableCell>
				<TableCell :class="rowClass">
					<Skeleton class="w-5/6 h-5" />
				</TableCell>
				<TableCell v-for="(gradeType, i) in gradeTypes" :key="i" :class="rowClass">
					<Skeleton class="w-5/6 h-5" />
				</TableCell>
			</TableRow>
		</TableBody>
	</Table>
	<Skeleton v-else class="w-full h-64" />
</template>