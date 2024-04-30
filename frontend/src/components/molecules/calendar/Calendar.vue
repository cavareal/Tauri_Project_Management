<script setup lang="ts">
import { computed, h, ref } from 'vue'
import { CalendarDate, DateFormatter, getLocalTimeZone, parseDate, today } from '@internationalized/date'
import { Calendar } from '@/components/ui/calendar'
import { Button } from '@/components/ui/button'
import {
    FormControl,
    FormDescription,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from '@/components/ui/form'
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover'

const df = new DateFormatter('en-US', {
    dateStyle: 'long',
})



const placeholder = ref()

const { handleSubmit, setValues, values } = null

const value = computed({
    get: () => values.dob ? parseDate(values.dob) : undefined,
    set: val => val,
})

const onSubmit = handleSubmit(() => {
    console.log(values)
    console.log(value)
})
</script>

<template>
    <form class="space-y-8" @submit="onSubmit">
        <FormField name="dob">
            <FormItem class="flex flex-col">
                <FormLabel>Date of birth</FormLabel>
                <Popover>
                    <PopoverTrigger as-child>
                        <FormControl>
                            <Button variant="outline">
                                <span>Pick a date</span>
                            </Button>
                            <input hidden>
                        </FormControl>
                    </PopoverTrigger>
                    <PopoverContent class="w-auto p-0">
                        <Calendar v-model:placeholder="placeholder" v-model="value" calendar-label="Date of birth"
                            initial-focus :min-value="new CalendarDate(1900, 1, 1)" @update:model-value="(v) => {
                                console.log(v)
                                if (v) {
                                    setValues({
                                        dob: v.toString(),
                                    })
                                }
                                else {
                                    setValues({
                                        dob: '',
                                    })
                                }
                            }" />
                    </PopoverContent>
                </Popover>
                <FormMessage />
            </FormItem>
        </FormField>
        <Button type="submit">
            Submit
        </Button>
    </Form>
</template>