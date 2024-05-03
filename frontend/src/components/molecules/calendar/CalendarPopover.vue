<script setup lang="ts">
import { computed, h, ref, watch } from 'vue'
import { CalendarDate, DateFormatter, getLocalTimeZone, parseDate, today } from '@internationalized/date'
import { toDate } from 'radix-vue'
import { CalendarCheck } from "lucide-vue-next"
import { z } from 'zod'
import { Calendar } from '@/components/ui/calendar'
import { Button } from '@/components/ui/button'
import { useForm } from 'vee-validate'

import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover'


const props = defineProps<{
    minValue: CalendarDate | undefined,
    maxValue: CalendarDate | undefined,
}>()
// watch(props.maxValue, (newValue, oldValue) => {
//         console.log(newValue, oldValue)
//     });


const emit = defineEmits(['update:dateValue']);

function onDateSelected(selectedDate: CalendarDate | undefined ) {
    emit('update:dateValue', selectedDate);
}


const df = new DateFormatter('fr-FR', {
    dateStyle: 'long',
})

const formSchema = z.object({
    dob: z.string().refine(v => v, { message: 'Une date est requise !' }),
})

const placeholder = ref()

const { setValues, values } = useForm({
    validationSchema: formSchema,
})

const value = computed({
    get: () => values.dob ? parseDate(values.dob) : undefined,
    set: val => val,
})

</script>

<template>
    <Popover>
        <PopoverTrigger as-child>
            <Button variant="outline" class="w-[240px] ps-3 text-start font-normal">
                <span>{{ value ? df.format(toDate(value)) : "Choisissez une date" }}</span>
                <CalendarCheck class="ms-auto h-4 w-4 opacity-50" />
            </Button>
            <input hidden>
        </PopoverTrigger>
        <PopoverContent class="w-auto p-0">
            <Calendar v-model:placeholder="placeholder" v-model="value" calendar-label="Sprint date" initial-focus
                :min-value="props.minValue" :max-value="props.maxValue" @update:model-value="(v) => {
                    if (v) {
                        setValues({
                            dob: v.toString(),
                        })
                        onDateSelected(parseDate(v.toString()))
                    }
                    else {
                        setValues({
                            dob: '',
                        })
                        onDateSelected(undefined)
                    }
                }" />
        </PopoverContent>
    </Popover>
</template>
