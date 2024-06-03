<script setup lang="ts">
import { useQuery } from "@tanstack/vue-query"
import { getFlagsByConcernedTeam } from "@/services/flag"
import { Cookies } from "@/utils/cookie"
import { getStudentById } from "@/services/student"
import SwitchStudentsFlag from "@/components/organisms/teams/switch-student/SwitchStudentsFlag.vue"

const { data: flags } = useQuery({
	queryKey: ["flagForConcerned"],
	queryFn: async() => {
		const user = await getStudentById(Cookies.getUserId())
		return getFlagsByConcernedTeam(user.team!.id)
	}
})

</script>

<template>
  <SwitchStudentsFlag v-for="flag in flags" :key="flag" :flag="flag"/>
</template>

<style scoped>

</style>