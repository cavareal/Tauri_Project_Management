<script setup lang="ts">

import { AddSprintDialog } from "."
import { CalendarPlus } from "lucide-vue-next"
import type { CalendarDate } from "@internationalized/date"
import { Column } from "@/components/atoms/containers"

const emitAdd = defineEmits(["add:sprint"])

const props = defineProps<{
	firstSprint: boolean,
    lastSprintEndDate: CalendarDate | undefined,
	lastSprintOrder: number;
}>()

const TITLE = props.firstSprint ? "Vous n'avez pas encore crée de sprint, cliquez-ici pour ajouter le premier" : "Cliquez-ici pour ajouter un sprint"

</script>

<template>
    <div class="border border-gray-300 border-dashed rounded-lg hover:bg-gray-100 flex justify-center flex-col items-stretch">
        <AddSprintDialog @add:sprint="emitAdd('add:sprint')" :lastSprintEndDate="lastSprintEndDate" :lastSprintOrder="lastSprintOrder">
            <Column class="items-center py-4 gap-2">
				<CalendarPlus class="size-12 stroke-1 text-dark-blue" />
                <p class="text-dark-blue">{{ TITLE }}</p>
            </Column>
        </AddSprintDialog>
    </div>
</template>