<script setup lang="ts">

import { CustomDialog } from "@/components/molecules/dialog"
import { Button } from "@/components/ui/button"
import { Row } from "@/components/atoms/containers"
import { Input } from "@/components/ui/input"
import { ref } from "vue"
import { Cookies } from "@/utils/cookie"
import { getTeamByUserId } from "@/services/team-service"
import { getStudentsByTeamId } from "@/services/student-service"
import { hasPermission } from "@/services/user-service"
import { getStudentBonuses } from "@/services/bonus-service"
import { DialogClose } from "@/components/ui/dialog"
import { useQuery } from "@tanstack/vue-query"

let bonus = ref(["0", "0", "0", "0", "0", "0", "0", "0"])
const names = ["Alice", "Bob", "Charlie", "David", "Emma", "Frank", "Grace", "Henry"]

const limitedBonus = hasPermission("LIMITED_BONUS_MALUS")
const unlimitedBonus = hasPermission("GIVE_UNLIMITED_BONUS_MALUS")

const currentUserId = Cookies.getUserId()

const { data: team } = useQuery({ queryKey: ["team", currentUserId], queryFn: async() => getTeamByUserId(currentUserId) })

const { data: teamStudents } = useQuery({ queryKey: ["team-students", team.value?.id], queryFn: async() => {
	if (!team.value) return
	return await getStudentsByTeamId(team.value.id)
} })

const { data: studentBonuses } = useQuery({ queryKey: ["student-bonuses"], queryFn: async() => {
	if (!teamStudents.value) return
	return await Promise.all(teamStudents.value.map(student => getStudentBonuses(student.id)))
} })

const handleBonusInput = (event: InputEvent, index: number) => {
	if (!studentBonuses.value) return

	const inputNote = parseInt((event.target as HTMLInputElement).value)
	if (unlimitedBonus) {
		studentBonuses.value[index][1].value = inputNote
	} else if (inputNote > 4) {
		studentBonuses.value[index][0].value = 4
	} else if (inputNote < -4) {
		studentBonuses.value[index][0].value = -4
	} else {
		studentBonuses.value[index][0].value = inputNote
	}
}

const DIALOG_DESCRIPTION = "Vous pouvez ajuster les bonus et malus des membres de votre équipe. Attention, une fois que vous les avez validés, "
	+ "vous ne pouvez plus les modifier. Cependant, si un membre de votre équipe modifie de nouveau les bonus, vous pourrez les valider à nouveau."

</script>

<template>
	<CustomDialog title="Bonus et malus de votre équipe" :description="DIALOG_DESCRIPTION">
		<template #trigger>
			<Button variant="default">Voir les bonus</Button>
		</template>

		<Row v-if="studentBonuses" class="flex-wrap">
			<Row v-for="(student, index) in teamStudents" :key="student.id" class="grid grid-cols-3 items-center gap-4 mb-2 w-1/2">
				<Label>{{ student.name }}</Label>
				<Input v-if="unlimitedBonus" v-model="studentBonuses[index][1].value" type="number"
					@input="handleBonusInput($event, index)" />
				<Input v-if="limitedBonus" v-model="studentBonuses[index][0].value" type="number" min="-4" max="4"
					@input="handleBonusInput($event, index)" />
			</Row>
		</Row>

		<template #footer>
			<DialogClose>
				<Button variant="default">Valider</Button>
			</DialogClose>
		</template>
	</CustomDialog>
</template>
<style scoped></style>