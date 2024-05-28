<script setup lang="ts">
import { defineProps } from "vue"
import { VueLatex } from "vatex"


const props = defineProps<{
	expression: string;
}>()

</script>

<template>
	<div class="relative group">
		<slot></slot>
		<div class="absolute top-full mt-2 w-max max-w-xs px-3 py-2 bg-gray-700 text-white text-sm rounded-lg opacity-0 group-hover:opacity-100 transition-opacity">
			<VueLatex :expression="props.expression" display-mode />
		</div>
	</div>
</template>

<style scoped>
.group:hover .group-hover\:opacity-100 {
	opacity: 1;
}
</style>