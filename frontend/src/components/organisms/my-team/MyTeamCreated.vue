<script setup lang="ts">
import type { Team } from "@/types/team"
import MyTeamAccordion from "@/components/organisms/my-team/MyTeamAccordion.vue"

const props = defineProps({
	phase: {
		type: String,
		required: true
	},
	team: {
		type: Object as () => Team,
		required: true
	}
})
</script>

<template>
  <div class="p-10">
      <MyTeamAccordion :phase="props.phase" :team="props.team"/>
  </div>
</template>

<style scoped>

</style>