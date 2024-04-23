<script setup lang="ts">

import { cn } from "@/utils/style"
import { Check, XIcon } from "lucide-vue-next"

const props = defineProps<{
	checked?: boolean
}>()

const style = cn(
	"w-4 h-4 fill-none",
	props.checked ? "stroke-green-700" : "stroke-red-700"
)

</script>

<template>
	<Check v-if="props.checked" :class="style" />
	<XIcon v-else :class="style" />
</template>