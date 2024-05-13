<script setup lang="ts">
import AddSprintDialog from "./AddSprintDialog.vue"
import { CalendarPlus } from "lucide-vue-next"
import { CalendarDate, parseDate } from '@internationalized/date'
    
const emitAdd = defineEmits(["add:sprint"])

const props = defineProps<{
    title: String,
    lastSprintEndDate: CalendarDate | undefined,
	lastSprintOrder: number;
}>()
</script>

<template>
    <div class="border border-gray-300 border-dashed rounded-lg my-4 hover:bg-gray-100 flex justify-center flex-col items-stretch">   
        <AddSprintDialog @add:sprint="emitAdd('add:sprint')" class="flex flex-col items-center" :lastSprintEndDate="lastSprintEndDate" :lastSprintOrder="lastSprintOrder">
            <div class="flex flex-col items-center p-2">
                <CalendarPlus class="w-10 h-10 my-2" />
                <p class=" my-2">{{ title }}</p>
            </div>
        </AddSprintDialog>
    </div>
</template>