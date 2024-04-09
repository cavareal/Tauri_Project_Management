<script setup lang="ts">
import {
	Dialog,
	DialogContent,
	DialogDescription,
	DialogFooter,
	DialogHeader,
	DialogTitle,
	DialogTrigger,
	DialogClose
} from "@/components/ui/dialog"
import {
	Select,
	SelectContent,
	SelectGroup,
	SelectItem,
	SelectLabel,
	SelectTrigger,
	SelectValue
} from "@/components/ui/select"
import { Button } from "@/components/ui/button"
import { Column, Row } from "@/components/atoms/containers"
import Input from "@/components/ui/input/Input.vue"
import Label from "@/components/ui/label/Label.vue"
import { getTeamById, setTeamLeader, setTeamName } from "@/services/team-service"
import { ref, watch } from "vue"
import type { Team } from "@/types/team"
import { Skeleton } from "@/components/ui/skeleton"
import type { User } from "@/types/user"
import { getUsersByRole } from "@/services/user-service"

const props = defineProps<{
	teamId: number
}>()

const team = ref<Team | null>(null)
const supervisors = ref<User[] | null>(null)
const teamLeaderId = ref<string | undefined>(undefined)

watch(() => { }, async() => {
	team.value = await getTeamById(props.teamId)
	supervisors.value = await getUsersByRole("SUPERVISING_STAFF")
	teamLeaderId.value = team.value?.leader?.id.toString() ?? undefined
}, { immediate: true })

const updateTeam = async() => {
	await (async() => {
		if (!team.value) return
		await setTeamName(team.value.id, team.value.name)
		teamLeaderId.value && await setTeamLeader(team.value.id, teamLeaderId.value)
	})().then(() => location.reload())
}
</script>

<template>
	<Dialog>
		<DialogTrigger>
			<slot />
		</DialogTrigger>

		<DialogContent>
			<DialogHeader>
				<DialogTitle>Modifier une équipe</DialogTitle>
				<DialogDescription>Vous pouvez modifier le nom de l'équipe ainsi que son professeur référent.
				</DialogDescription>
			</DialogHeader>

			<Column class="gap-4 my-4" v-if="team && supervisors">
				<Row class="gap-4 items-center">
					<Label for="teamName" class="text-right w-1/3">Nom de l'équipe</Label>
					<Input id="teamName" type="text" v-model="team.name" class="w-2/3" />
				</Row>
				<Row class="gap-4 items-center">
					<Label for="teamLeader" class="text-right w-1/3">Professeur référent</Label>
					<Select id="teamLeader" v-model="teamLeaderId">
						<SelectTrigger class="w-2/3">
							<SelectValue placeholder="Sélectionnez un professeur" />
						</SelectTrigger>
						<SelectContent>
							<SelectItem v-for="supervisor in supervisors" :key="supervisor.id" :value="supervisor.id.toString()">
								{{ supervisor.name }}
							</SelectItem>
						</SelectContent>
					</Select>
				</Row>
			</Column>
			<Skeleton v-else class="w-full h-16" />

			<DialogFooter class="space-x-2">
				<DialogClose>
					<Button variant="outline">
						Annuler
					</Button>
				</DialogClose>
				<DialogClose>
					<Button type="submit" @click="updateTeam">
						Confirmer
					</Button>
				</DialogClose>
			</DialogFooter>
		</DialogContent>
	</Dialog>
</template>