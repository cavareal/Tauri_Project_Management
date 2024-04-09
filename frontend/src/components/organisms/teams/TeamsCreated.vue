<script setup lang="ts">
import TeamAccordion from "@/components/organisms/teams/TeamAccordion.vue"
const props = defineProps({
	phase: {
		type: String,
		required: true
	}
})
</script>

<template>
	<TeamAccordion :phase="props.phase" />
</template>