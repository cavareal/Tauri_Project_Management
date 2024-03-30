<script setup lang="ts">
import { ref } from "vue"
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion"

const defaultValue = "item-1"

const accordionItems = [
	{ value: "item-1", title: "Team 1", content: "content" },
	{ value: "item-2", title: "Team 2", content: "content" },
	{ value: "item-3", title: "Team 3", content: "Content" }
]

</script>

<template>
    <div class="border border-gray-300 rounded-lg p-2 md:p-3 my-4">
        <Accordion type="single" class="w-full" collapsible :default-value="defaultValue">
            <AccordionItem v-for="item in accordionItems" :key="item.value" :value="item.value">
                <AccordionTrigger>{{ item.title }}</AccordionTrigger>
                <AccordionContent>
                    {{ item.content }}
                </AccordionContent>
            </AccordionItem>
        </Accordion>
    </div>
</template>