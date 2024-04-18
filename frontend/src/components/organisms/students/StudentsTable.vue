<script setup lang="ts">

import { Table, TableBody, TableCell, TableRow, TableHead, TableHeader } from "@/components/ui/table"
import { Skeleton } from "@/components/ui/skeleton"
import { cn } from "@/utils/style"
import CheckIcon from "@/components/atoms/icons/CheckIcon.vue"
import GenderIcon from "@/components/atoms/icons/GenderIcon.vue"
import type { GradeType } from "@/types/grade-type"
import type { Student } from "@/types/student"
import type { Grade } from "@/types/grade"

const rowClass = cn("py-2 h-auto")

defineProps<{
	students: Student[] | null
	gradeTypes: GradeType[] | null
	grades: Grade[] | null
}>()

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
				<TableCell :class="rowClass">
					<GenderIcon :gender="student.gender" />
				</TableCell>
				<TableCell :class="rowClass">
					<CheckIcon :checked="student.bachelor ?? false" />
				</TableCell>
				<TableCell v-for="(gradeType, i) in gradeTypes" :key="i" :class="rowClass">
					<Skeleton v-if="!grades" class="w-5/6 h-5" />
					<span v-else>
						<!-- eslint-disable-next-line max-len -->
						{{ grades?.find(grade => grade.student?.id === student.id && grade.gradeType.id === gradeType.id)?.value.toPrecision(4) ?? "" }}
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