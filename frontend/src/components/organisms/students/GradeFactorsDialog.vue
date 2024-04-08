<script setup lang="ts">
import {
	Dialog,
	DialogContent,
	DialogDescription,
	DialogFooter,
	DialogHeader,
	DialogTitle,
	DialogTrigger,
	DialogClose
} from "@/components/ui/dialog"
import { Column, Row } from "@/components/atoms/containers"
import { Button } from "@/components/ui/button"
import { type GradeType, GradeTypeSchema } from "@/types/grade-type"
import { apiQuery } from "@/utils/api"
import { ref, watch } from "vue"
import { z } from "zod"
import { Skeleton } from "@/components/ui/skeleton"
import { Input } from "@/components/ui/input"

const open = ref(false)
const gradeTypes = ref<GradeType[] | null>(null)
const submitLoading = ref(false)

const fetchGradeTypes = async() => {
	const data = await apiQuery({
		responseSchema: z.array(GradeTypeSchema),
		method: "GET",
		route: "grade_types",
		delay: 5000
	})
	if (data.status === "success") {
		gradeTypes.value = data.data.filter(gradeType => gradeType.imported && !["mean", "average"].includes(gradeType.name.toLowerCase()))
	}
}

watch(() => { }, fetchGradeTypes, { immediate: true })

const onInputValueChange = (e: Event, gradeTypeId: number) => {
	const input = e.target as HTMLInputElement
	const value = parseFloat(input.value)
	if (isNaN(value) || value < 0) return

	const gradeType = gradeTypes.value?.find(gradeType => gradeType.id === gradeTypeId)
	if (gradeType) gradeType.factor = value
}

const updateGradeFactors = async() => {
	if (!gradeTypes.value) return
	submitLoading.value = true

	await Promise.all(gradeTypes.value?.map((gradeType) => apiQuery({
		method: "PATCH",
		responseSchema: GradeTypeSchema,
		route: `grade_types/${gradeType.id}`,
		body: { factor: gradeType.factor }
	}))).then(() => location.reload())

	submitLoading.value = false
	open.value = false
}
</script>

<template>
	<Dialog>
		<DialogTrigger>
			<slot />
		</DialogTrigger>

		<DialogContent>
			<DialogHeader>
				<DialogTitle>Modifier les coefficients</DialogTitle>
				<DialogDescription>Les coefficients des notes importées sont utilisés lors de la génération des équipes.
				</DialogDescription>
			</DialogHeader>

			<Column v-if="gradeTypes" class="items-stretch gap-2">
				<Row v-for="(gradeType, i) in gradeTypes" :key="i" class="items-center">
					<p class="w-1/2">{{ gradeType.name }}</p>
					<Input class="w-1/2" :default-value="gradeType.factor"
						:onchange="(e: Event) => onInputValueChange(e, gradeType.id)" />
				</Row>
			</Column>
			<Skeleton v-else class="w-full h-56" />

			<DialogFooter>
				<DialogClose>
					<Button type="submit" @click="updateGradeFactors" :disabled="submitLoading">
						Mettre à jour
					</Button>
				</DialogClose>
			</DialogFooter>
		</DialogContent>
	</Dialog>
</template>