<script setup lang="ts">
import { computed, h, ref, watch, type Ref } from "vue"
import type { CalendarDate } from "@internationalized/date"
import { DateFormatter, getLocalTimeZone, parseDate, today } from "@internationalized/date"
import { toDate, type Grid } from "radix-vue"
import { CalendarCheck } from "lucide-vue-next"
import { z } from "zod"
import { Calendar } from "@/components/ui/calendar"
import { Button } from "@/components/ui/button"

import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"


const props = defineProps<{
    minValue: CalendarDate | undefined,
    maxValue: CalendarDate | undefined,
    actualValue: CalendarDate | undefined,
}>()


const emit = defineEmits(["update:dateValue"])

function onDateSelected(selectedDate: CalendarDate | undefined) {
	emit("update:dateValue", selectedDate)
}

const df = new DateFormatter("fr-FR", {
	dateStyle: "long"
})

const formSchema = z.object({
	dob: z.string().refine(v => v, { message: "Une date est requise !" })
})

const placeholder = ref()

const value = ref({
	actual: props.actualValue
}) as Ref<Grid>

</script>

<template>
    <Popover>
        <PopoverTrigger as-child>
            <Button variant="outline" class="w-[250px] ps-3 text-start font-normal">
                <span>{{ value.actual ? df.format(toDate(value.actual)) : "Choisissez une date" }}</span>
                <CalendarCheck class="ms-auto h-4 w-4 opacity-50" />
            </Button>
            <input hidden>
        </PopoverTrigger>
        <PopoverContent class="w-auto p-0">
            <Calendar v-model:placeholder="placeholder" v-model="value.actual" calendar-label="Sprint date" initial-focus
                :min-value="props.minValue" :max-value="props.maxValue" @update:model-value="(v) => {
                    if (v) {
                        onDateSelected(parseDate(v.toString()))
                    }
                    else {
                        onDateSelected(undefined)
                    }}
                " />
        </PopoverContent>
    </Popover>
</template>