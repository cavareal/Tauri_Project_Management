<script setup lang="ts">
import TeamAccordion from "@/components/organisms/Teams/TeamAccordion.vue"
const props = defineProps({
	phase: {
		type: String,
		required: true
	}
})
</script>

<template>
	<div class="p-10">
      <TeamAccordion :phase="props.phase"/>
  </div>
</template>