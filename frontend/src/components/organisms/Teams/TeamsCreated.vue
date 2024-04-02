<script setup lang="ts">
import TeamTable from "@/components/organisms/TeamTable.vue"
import type { Team } from "@/type/Team"
import { ref } from "vue"
import { onMounted } from "vue"
import { getTeams } from "@/service/TeamService"

const teams = ref<Team[]>([])

onMounted(async() => {
	const data = await getTeams()
	teams.value = data
	console.log(data)
})
</script>

<template>
    <div v-for = "(team, i) in teams" :key="i" class="border border-gray-300 rounded-lg p-2 md:p-3 my-4">
      <TeamTable :teamId="team.id"></TeamTable>
    </div>
</template>