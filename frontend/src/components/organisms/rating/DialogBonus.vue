<script setup lang="ts">

import { CustomDialog } from "@/components/molecules/dialog"
import { Button } from "@/components/ui/button"
import { Column, Row } from "@/components/atoms/containers"
import { Input } from "@/components/ui/input"
import { ref } from "vue"
import { DialogClose } from "@/components/ui/dialog"

let bonus = ref(["0", "0", "0", "0", "0", "0", "0", "0"])
const names = ["Alice", "Bob", "Charlie", "David", "Emma", "Frank", "Grace", "Henry"]
const firstColumn = names.slice(0, 4)
const secondColumn = names.slice(4)

const handleBonusInput = (event: InputEvent, index: number) => {
	const inputNote = parseInt((event.target as HTMLInputElement).value)
	if (inputNote > 4) {
		bonus.value[index] = String(4)
	} else if (inputNote < -4) {
		bonus.value[index] = String(-4)
	} else {
		bonus.value[index] = String(inputNote)
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
					<Input v-model="bonus[index]" type="number" min="-4" max="4" @input="handleBonusInput($event, index)"/>
				</Row>
			</Column>
			<Column>
				<Row v-for="(name, index) in secondColumn" :key="index" class="grid grid-cols-3 items-center gap-4 mb-2">
					<Label>{{ name }}</Label>
					<Input v-model="bonus[index + 4]" type="number" min="-4" max="4" @input="handleBonusInput($event,index + 4)"/>
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