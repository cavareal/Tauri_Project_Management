<script setup lang="ts">
import { computed, ref } from "vue"
import AddSprintDialog from "./AddSprintDialog.vue"
import { CalendarPlus } from "lucide-vue-next"
import { Column } from "@/components/atoms/containers"
import { InfoText, Subtitle } from "@/components/atoms/texts"
import Text from "@/components/atoms/texts/Text.vue"
import { cn } from "@/utils/style"

const nbSprints = ref(0)

const IS_SPRINT = "Cliquez-ici pour ajouter un sprint"
const ISNT_SPRINT = "Vous n'avez pas encore crée de sprint, cliquez-ici pour ajouter le premier"

const emitAdd = defineEmits(["add:sprint"])

const style = computed(() => cn(
	"items-center justify-center",
	"p-6 gap-2",
	"rounded-lg border transition-all cursor-pointer border-dashed",
	"bg-white hover:bg-slate-50"
))

</script>

<template>
	<AddSprintDialog @add:sprint="emitAdd('add:sprint')">
		<Column :class="style">
			<CalendarPlus class="w-16 h-16 stroke-[1.2]" />
			<Text>{{ nbSprints != 0 ? IS_SPRINT : ISNT_SPRINT }}</Text>
		</Column>
	</AddSprintDialog>
</template>