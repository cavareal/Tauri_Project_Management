<script setup lang="ts">
import type { Flag } from "@/types/flag"
import { getAllStudents } from "@/services/student"
import { useQuery } from "@tanstack/vue-query"
import { getValidationFlagsByFlagId } from "@/services/validationFlag"
import { Column, Row } from "@/components/atoms/containers"
import { InfoText, Subtitle, Text } from "@/components/atoms/texts"
import { ArrowLeftRight } from "lucide-vue-next"
import { User } from "lucide-vue-next"

const props = defineProps<{
  flag: Flag
}>()

const { data: validations } = useQuery({
	queryKey: ["validationsFlag"],
	queryFn: async() => {
		const students = await getAllStudents()
		const validationFlags = await getValidationFlagsByFlagId(props.flag.id)
		let nbValidationNeeded = 0
		let nbValidated = 0
		let nbRefused = 0
		if (students.length !== 0) {
			const firstTeamStudents = students.filter(student => student.team && student.team.id === props.flag.firstStudent!.team!.id)
			const secondTeamStudents = students.filter(student => student.team && student.team.id === props.flag.secondStudent!.team!.id)
			nbValidationNeeded = firstTeamStudents.length + secondTeamStudents.length - validationFlags.length
			nbValidated = validationFlags.filter(flag => flag.confirmed).length
			nbRefused = validationFlags.filter(flag => !flag.confirmed).length
		}
		return { nbValidationNeeded, nbValidated, nbRefused }
	}
})
</script>

<template>
  <Row class="items-center gap-4">
    <Column class="items-start justify-center gap-1">
      <Subtitle>Demande de modification d'équipe faite par {{ props.flag.author.name}}</Subtitle>
      <InfoText>
        {{props.flag.description}}
      </InfoText>
      <Row class="justify-between">
        <Text>{{ props.flag.firstStudent!.name }} - {{ props.flag.firstStudent!.team!.name}}</Text>
        <ArrowLeftRight/>
        <Text>{{ props.flag.secondStudent!.name }} - {{ props.flag.secondStudent!.team!.name}}</Text>
      </Row>
      <Row v-if="validations">
        <User v-for="n in validations.nbValidated"  :key="n" class="accent-light-blue"/>
        <User v-for="n in validations.nbRefused"  :key="n" class="accent-tauri-red"/>
        <User v-for="n in validations.nbValidationNeeded"  :key="n" class="accent-light-grey"/>
      </Row>
    </Column>
  </Row>

</template>

<style scoped>

</style>