<script setup lang="ts">

import { Column, Row } from "@/components/atoms/containers"
import { useMutation } from "@/utils/api"
import { Skeleton } from "@/components/ui/skeleton"
import { Input } from "@/components/ui/input"
import { CustomDialog } from "@/components/molecules/dialog"
import { LoadingButton } from "@/components/molecules/buttons"
import { updateGradeTypeFactor } from "@/services/grade-type-service"
import { ErrorText } from "@/components/atoms/texts"
import type { GradeType } from "@/types/grade-type"
import { computed, ref } from "vue"
import { DialogClose } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"

const DIALOG_TITLE = "Modifier les coefficients"
const DIALOG_DESCRIPTION = "Les coefficients des notes importées sont utilisés lors de la génération des équipes."

const open = ref(false)

const props = defineProps<{
	gradeTypes: GradeType[] | null
}>()

const filteredGradeTypes = computed(() => props.gradeTypes?.filter(gradeType => !["mean", "average"].includes(gradeType.name.toLowerCase())))

const onInputValueChange = (e: Event, gradeTypeId: number) => {
	const input = e.target as HTMLInputElement
	const value = parseFloat(input.value)
	if (isNaN(value) || value < 0) return

	const gradeType = filteredGradeTypes.value?.find(gradeType => gradeType.id === gradeTypeId)
	if (gradeType) gradeType.factor = value
}

const emits = defineEmits(["update:factors"])

const { loading, error, mutate: upgrade } = useMutation(async() => {
	if (!filteredGradeTypes.value) return
	await Promise.all(filteredGradeTypes.value.map(gradeType => updateGradeTypeFactor(gradeType.id, gradeType.factor)))
		.then(() => open.value = false)
		.then(() => emits("update:factors"))
})

</script>

<template>
	<CustomDialog :title="DIALOG_TITLE" :description="DIALOG_DESCRIPTION" v-model:open="open">
		<template #trigger>
			<slot />
		</template>

		<Column v-if="filteredGradeTypes" class="items-stretch gap-2">
			<Row v-for="(gradeType, i) in filteredGradeTypes" :key="i" class="items-center">
				<Text class="w-1/2">{{ gradeType.name }}</Text>
				<Input class="w-1/2" :default-value="gradeType.factor" :onchange="(e: Event) => onInputValueChange(e, gradeType.id)" />
			</Row>
		</Column>
		<Skeleton v-else class="w-full h-56" />
		<ErrorText v-if="error">Une erreur est survenue.</ErrorText>

		<template #footer>
			<DialogClose v-if="!loading">
				<Button variant="outline">Annuler</Button>
			</DialogClose>
			<LoadingButton type="submit" @click="upgrade" :loading="loading">
				Mettre à jour
			</LoadingButton>
		</template>
	</CustomDialog>
</template>