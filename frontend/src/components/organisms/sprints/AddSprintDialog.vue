<script setup lang="ts">

import { ref } from "vue"
import { CustomDialog, DialogClose } from "@/components/molecules/dialog"
import { Button } from "@/components/ui/button"
import CalendarPopover from "@/components/molecules/calendar/CalendarPopover.vue"
import { Row } from "@/components/atoms/containers"
import { CalendarDate } from '@internationalized/date'
import { useMutation } from "@tanstack/vue-query"
import LoadingButton from "@/components/molecules/buttons/LoadingButton.vue"
import { addSprint } from "@/services/sprint-service"
import {
	Select,
	SelectContent,
	SelectGroup,
	SelectItem,
	SelectLabel,
	SelectTrigger,
	SelectValue,
} from '@/components/ui/select'
import { Info } from "lucide-vue-next"
import {
	Tooltip,
	TooltipContent,
	TooltipProvider,
	TooltipTrigger
} from '@/components/ui/tooltip'

const DIALOG_TITLE = "Ajouter un sprint"
const DIALOG_DESCRIPTION = "Pour ajouter un sprint, vous devez spécifier les dates de début et de fin, ainsi que le type de sprint."

const open = ref(false)

const emits = defineEmits(["add:sprint"]);

const startDate = ref<CalendarDate>();
const endDate = ref<CalendarDate>();

var minDate = new CalendarDate(1900, 1, 1)
var maxDate = new CalendarDate(2100, 1, 1)


const { error, isPending, mutate: add } = useMutation({
	mutationKey: ["add-sprint"], mutationFn: async () => {
		const start_date = startDate.value?.day + '/' + startDate.value?.month + '/' + startDate.value?.year
		const end_date = endDate.value?.day + '/' + endDate.value?.month + '/' + endDate.value?.year

		const sprint = { startDate: start_date, endDate: end_date, type: "ouai" };

		await addSprint(sprint)
			.then(() => open.value = false)
			.then(() => emits("add:sprint"))
	}
})

</script>

<template>
	<CustomDialog :title="DIALOG_TITLE" :description="DIALOG_DESCRIPTION" v-model:open="open">
		<template #trigger>
			<slot />
		</template>

		<div>
			<Row class="flex items-center justify-between my-2">
				<Label>Date de début</Label>
				<CalendarPopover v-model="startDate" class="w-[250px]"
					@update:dateValue="(newDate: CalendarDate | undefined) => startDate = newDate" :min-value="minDate"
					:max-value="endDate" />
			</Row>
			<Row class="flex items-center justify-between my-2">
				<Label>Date de fin</Label>
				<CalendarPopover v-model="endDate"
					@update:dateValue="(newDate: CalendarDate | undefined) => endDate = newDate" :min-value="startDate"
					:max-value="maxDate" />
			</Row>
			<Row class="flex items-center justify-between my-2">
				<Label class="flex">Type de sprint
					<TooltipProvider>
						<Tooltip>
							<TooltipTrigger>
								<Info class="w-4 h-4 ml-1" />
							</TooltipTrigger>
							<TooltipContent>
								<p>Ouai les sprints</p>
							</TooltipContent>
						</Tooltip>
					</TooltipProvider>
				</Label>

				<Select>
					<SelectTrigger class="w-[250px]">
						<SelectValue placeholder="Type de fin de sprint" />
					</SelectTrigger>
					<SelectContent>
						<SelectGroup>
							<SelectItem value="Sprint Normal">
								Sprint Normal
							</SelectItem>
							<SelectItem value="Sprint Final">
								Sprint Final
							</SelectItem>
						</SelectGroup>
					</SelectContent>
				</Select>
			</Row>
		</div>



		<ErrorText v-if="error" class="mb-2">Une erreur est survenue lors de l'ajout du sprint.</ErrorText>


		<template #footer>
			<DialogClose>
				<Button variant="outline">Annuler</Button>
			</DialogClose>
			<LoadingButton type="submit" class="flex items-center" :loading="isPending" @click="add">
				Continuer
			</LoadingButton>
		</template>
	</CustomDialog>
</template>