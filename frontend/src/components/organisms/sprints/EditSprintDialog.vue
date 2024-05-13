<script setup lang="ts">

import { ref } from "vue"
import { CustomDialog, DialogClose } from "@/components/molecules/dialog"
import { ErrorText } from "@/components/atoms/texts"
import { Button } from "@/components/ui/button"
import CalendarPopover from "@/components/molecules/calendar/CalendarPopover.vue"
import { Row } from "@/components/atoms/containers"
import { CalendarDate, parseDate } from '@internationalized/date'
import { useMutation } from "@tanstack/vue-query"
import LoadingButton from "@/components/molecules/buttons/LoadingButton.vue"
import { updateSprint } from "@/services/sprint-service"
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
import { getCookie } from "@/utils/cookie"
import type { Sprint } from "@/types/sprint"
import { Input } from "@/components/ui/input"


const DIALOG_TITLE = "Modifier un sprint"
const DIALOG_DESCRIPTION = "Vous pouvez modifier les dates de début et de fin, ainsi que le type de sprint."

const open = ref(false)

const emits = defineEmits(["edit:sprint"]);

const props = defineProps<{
    sprint: Sprint,
}>()

const startDate = ref<CalendarDate>(formatDate(props.sprint.startDate));
const endDate = ref<CalendarDate>(formatDate(props.sprint.endDate));
const endType = ref<string>(props.sprint.endType);
const valuesEmpty = ref<boolean>(false);
const sprintOrder = ref<number>(props.sprint.sprintOrder);

var minDate = new CalendarDate(1900, 1, 1)
var maxDate = new CalendarDate(2100, 1, 1)
	
function formatDate(date: Date) {
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0'); 
    const year = date.getFullYear();
	return new CalendarDate(Number(year), Number(month), Number(day));
}


const { error, isPending, mutate: edit } = useMutation({
	mutationKey: ["edit-sprint"], mutationFn: async () => {

		if (!startDate.value || !endDate.value || !endType.value || !sprintOrder.value) {
			valuesEmpty.value = true;
			return;
		}
		valuesEmpty.value = false;

		const start_date = startDate.value?.year + '-' + startDate.value?.month.toString().padStart(2, '0') + '-' + startDate.value?.day.toString().padStart(2, '0')
		const end_date = endDate.value?.year + '-' + endDate.value?.month.toString().padStart(2, '0') + '-' + endDate.value?.day.toString().padStart(2, '0')
		const currentProjectId = getCookie("currentProject")

		const sprintData = { startDate: start_date, endDate: end_date, endType: endType.value, projectId: currentProjectId ?? "", sprintOrder: sprintOrder.value };

		await updateSprint(sprintData, props.sprint.id)
			.then(() => open.value = false)
			.then(() => emits("edit:sprint"))
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
				<CalendarPopover v-model="startDate" @update:dateValue="(newDate: CalendarDate ) => startDate = newDate" 
					:min-value="minDate" :max-value="endDate" :actual-value="startDate" />
			</Row>
			<Row class="flex items-center justify-between my-2">
				<Label>Date de fin</Label>
				<CalendarPopover v-model="endDate" @update:dateValue="(newDate: CalendarDate ) => endDate = newDate" 
					:min-value="startDate" :max-value="maxDate" :actual-value="endDate"/>
			</Row>
			<Row class="flex items-center justify-between my-2">
				<Label>Ordre de ce sprint</Label>
				<Input v-model="sprintOrder" type="number" min="1" class="w-[250px]" />
			</Row>
			<Row class="flex items-center justify-between my-2">
				<Label class="flex">Type de sprint
					<TooltipProvider>
						<Tooltip>
							<TooltipTrigger>
								<Info class="w-4 h-4 ml-1" />
							</TooltipTrigger>
							<TooltipContent>
								<p>Sprint Normal (Sprint planning, sprint, Sprint Review avec presentation et demonstration [soutenance], Sprint Retrospective)</p>
								<p>Sprint Non Noté (Sprint planning, sprint, possibilité de sprint review avec le client [non noté], Sprint Retrospective)</p>
								<p>Sprint Final (Sprint planning, sprint, Sprint Review avec presentation et demonstration [soutenance], Sprint Retrospective, Présentation Vidéo)</p>
							</TooltipContent>
						</Tooltip>
					</TooltipProvider>
				</Label>

				<Select v-model="endType">
					<SelectTrigger class="w-[250px]">
						<SelectValue placeholder="Type de fin de sprint" />
					</SelectTrigger>
					<SelectContent>
						<SelectGroup>
							<SelectItem value="NORMAL_SPRINT">
								Sprint Normal
							</SelectItem>
							<SelectItem value="UNGRADED_SPRINT">
								Sprint Non Noté
							</SelectItem>
							<SelectItem value="FINAL_SPRINT">
								Sprint Final
							</SelectItem>
						</SelectGroup>
					</SelectContent>
				</Select>
			</Row>
		</div>



		<ErrorText v-if="error" class="mb-2">Une erreur est survenue lors de la modification du sprint.</ErrorText>
		<ErrorText v-if="valuesEmpty" class="mb-2">Veuilliez bien remplir tous les champs</ErrorText>


		<template #footer>
			<DialogClose>
				<Button variant="outline">Annuler</Button>
			</DialogClose>
			<LoadingButton type="submit" class="flex items-center" :loading="isPending" @click="edit">
				Confirmer
			</LoadingButton>
		</template>
	</CustomDialog>
</template>