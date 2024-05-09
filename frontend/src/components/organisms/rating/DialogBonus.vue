<script setup lang="ts">

import { CustomDialog } from "@/components/molecules/dialog"
import { Button } from "@/components/ui/button"
import { Column, Row } from "@/components/atoms/containers"
import { Input } from "@/components/ui/input"
import { onMounted, ref } from "vue"
import { Cookies } from "@/utils/cookie"
import { getTeamByUserId } from "@/services/team-service"
import { getStudentsByTeamId } from "@/services/student-service"
import type { Student } from "@/types/student"
import { hasPermission } from "@/services/user-service"
import type { Bonus } from "@/types/bonus"
import { getStudentBonuses } from "@/services/bonus-service"
import { DialogClose } from "@/components/ui/dialog"

let bonus = ref(["0", "0", "0", "0", "0", "0", "0", "0"])
const names = ["Alice", "Bob", "Charlie", "David", "Emma", "Frank", "Grace", "Henry"]

const limitedBonus = hasPermission("LIMITED_BONUS_MALUS")
const unlimitedBonus = hasPermission("GIVE_UNLIMITED_BONUS_MALUS")

const currentUser = Cookies.getUserId()
const teamStudents = ref<Student[]>([])
const half = ref(0)
const studentNames = ref<string[]>([])
const studentBonuses = ref<Bonus[][]>([])
const firstColumn = ref<string[]>([])
const secondColumn = ref<string[]>([])

onMounted(async() => {
	const team = await getTeamByUserId(currentUser)
	teamStudents.value = await getStudentsByTeamId(team.id)
	half.value = Math.ceil(teamStudents.value.length / 2)
	studentNames.value = teamStudents.value.map(student => student.name)
	firstColumn.value = studentNames.value.slice(0, half.value)
	secondColumn.value = studentNames.value.slice(half.value)

	// Get bonuses for all students
	studentBonuses.value = await Promise.all(teamStudents.value.map(student => getStudentBonuses(student.id)))

})


const handleBonusInput = (event: InputEvent, index: number) => {
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

</script>

<template>
	<CustomDialog title="Bonus et malus de votre équipe" description="Vous pouvez ajuster les bonus et malus des membres de votre équipe. Attention, une fois que vous les avez validés, vous ne pouvez plus les modifier. Cependant, si un membre de votre équipe modifie de nouveau les bonus, vous pourrez les valider à nouveau.">
		<template #trigger>
			<Button variant="default">Voir les bonus</Button>
		</template>
		<div class="flex">
			<Column>
				<Row v-for="(name, index) in firstColumn" :key="index" class="grid grid-cols-3 items-center gap-4 mb-2">
					<Label>{{ name }}</Label>
					<Input v-if="unlimitedBonus" v-model="studentBonuses[index][1].value" type="number" @input="handleBonusInput($event, index)"/>
					<Input v-if="limitedBonus" v-model="studentBonuses[index][0].value" type="number" min="-4" max="4" @input="handleBonusInput($event, index)"/>
				</Row>
			</Column>
			<Column>
				<Row v-for="(name, index) in secondColumn" :key="index" class="grid grid-cols-3 items-center gap-4 mb-2">
					<Label>{{ name }}</Label>
					<Input v-if="unlimitedBonus" v-model="studentBonuses[index+half][1].value" type="number" @input="handleBonusInput($event, index+half)"/>
					<Input v-if="limitedBonus" v-model="studentBonuses[index + half][0].value" type="number" min="-4" max="4" @input="handleBonusInput($event,index + half)"/>
				</Row>
			</Column>
		</div>
		<template #footer>
			<DialogClose>
				<Button variant="default">Valider</Button>
			</DialogClose>
		</template>
	</CustomDialog>
</template>
<style scoped>

</style>