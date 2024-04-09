<script setup lang="ts">
import { type PropType, reactive, type Ref } from "vue"
import { ref } from "vue"
import { Ellipsis, Loader2, Pencil, Trash2 } from "lucide-vue-next"

import {
	Dialog,
	DialogClose,
	DialogContent,
	DialogDescription,
	DialogFooter,
	DialogHeader,
	DialogTitle,
	DialogTrigger
} from "@/components/ui/dialog"
import {
	Table,
	TableBody,
	TableCell,
	TableHead,
	TableHeader,
	TableRow
} from "@/components/ui/table"

import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"
import getCookie from "@/utils/cookiesUtils"
import {
	DropdownMenu,
	DropdownMenuContent,
	DropdownMenuItem,
	DropdownMenuTrigger
} from "@/components/ui/dropdown-menu"

const token = getCookie("token")
const selectedTeam = ref("")
let note = ref("")
interface Evaluation {
	team: string;
	gradeOverallPerformance: number;
}
interface Team {
	name: string;
}

const evaluations: Ref<Record<string, Evaluation[]>> = ref({})

const props = defineProps({
	listTeam: {
		type: Array as PropType<Team[]>,
		default: () => []
	}
})

const buttonsState = reactive({
	validate: true,
	loading: false
})

function redirect() : void {
	window.location.href = "/tauri/rating"
}

function addEvaluation() {
	if (!evaluations.value[selectedTeam.value]) {
		evaluations.value[selectedTeam.value] = []
	}
	const teamIndex = evaluations.value[selectedTeam.value].findIndex(e => e.team === selectedTeam.value)
	if (teamIndex !== -1) {
		evaluations.value[selectedTeam.value][teamIndex].gradeOverallPerformance = Number(note.value)
	} else {
		evaluations.value[selectedTeam.value].push({ team: selectedTeam.value, gradeOverallPerformance: Number(note.value) })
	}
}

const grades = async() => {
	try {
		buttonsState.validate = false
		buttonsState.loading = true
		const response = await fetch(import.meta.env.VITE_TAURI_API_URL + "grades/addGradeToTeam", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				Authorization: token || "null"
			},
			body: JSON.stringify(evaluations.value)
		})

		if (!response.ok) {
			throw new Error(`HTTP error! status: ${response.status}`)
		}
		buttonsState.loading = false
		buttonsState.validate = true
		redirect()
		// eslint-disable-next-line @typescript-eslint/no-unsafe-return
		return await response.json()
	} catch (error) {
		buttonsState.loading = false
		buttonsState.validate = true
		console.error(error)
	}
}

function handleNoteInput(event: InputEvent) {
	const inputNote = parseInt((event.target as HTMLInputElement).value)
	if (inputNote > 20) {
		note.value = String(20)
	} else {
		note.value = String(inputNote)
	}
}

function sendGrades() {
	console.log(evaluations.value)
	void grades()
}


</script>

<template>
	<div class="border border-gray-300 rounded-lg p-2 md:p-3 my-4">
		<h1 class="font-bold text-base">Evaluer la performance globale d'une équipe</h1>
		<div class="w-full pt-5 pb-2 px-0 flex justify-center">
			<Dialog>
				<DialogTrigger as-child>
					<Button class="text-white bg-primary hover:bg-primary/90">
						<span class="mx-5 my-0">Evaluer</span>
					</Button>
				</DialogTrigger>
				<DialogContent class="sm:max-w-[500px]">
					<DialogHeader>
						<DialogTitle>Evaluer la performance globale</DialogTitle>
						<DialogDescription>
							Sélectionnez l'équipe à évaluer et indiquez la note que vous souhaitez leur accorder
						</DialogDescription>
					</DialogHeader>
					<div class="grid gap-4 py-4">
						<div class="grid grid-cols-3 items-center gap-4">
							<Label for="equipe">Equipe :</Label>
							<select v-model="selectedTeam">
								<option value="" disabled selected hidden>Choisir une équipe</option>
								<option v-for="(team, index) in listTeam" :key="index" :value="team">
									{{ team }}
								</option>
							</select>
						</div>
						<div class="grid grid-cols-3 items-center gap-4">
							<Label for="note">Note :</Label>
							<Input id="note" type="number" min="0" max="20" v-model="note" @input="handleNoteInput"/>
						</div>
					</div>
					<DialogFooter>
						<DialogClose>
							<Button type="submit" variant="destructive"
									class="text-white bg-primary hover:bg-primary/90" @click="addEvaluation">
								Evaluer
							</Button>
						</DialogClose>
					</DialogFooter>
				</DialogContent>
			</Dialog>
		</div>
		<div class="w-1/2 mx-auto py-4">
			<Table>
				<TableHeader>
					<TableRow>
						<TableHead class="w-[200px]">
							Nom de l'équipe
						</TableHead>
						<TableHead>Note</TableHead>
					</TableRow>
				</TableHeader>
				<TableBody>
					<TableRow v-for="(teamEvaluations, teamName) in evaluations" :key="teamName">
						<TableCell>
							{{ teamName }}
						</TableCell>
						<TableCell v-for="(evaluation, index) in teamEvaluations" :key="index">
							{{ evaluation.gradeOverallPerformance }}
						</TableCell>
						<TableCell class="text-right">
							<DropdownMenu>
								<DropdownMenuTrigger>
									<Ellipsis/>
								</DropdownMenuTrigger>
								<DropdownMenuContent>
									<DropdownMenuItem>
										<Pencil/>
										Modifier
									</DropdownMenuItem>
									<DropdownMenuItem>
										<Trash2/>
										Supprimer
									</DropdownMenuItem>
								</DropdownMenuContent>
							</DropdownMenu>
						</TableCell>
					</TableRow>
				</TableBody>
			</Table>
		</div>
		<div class="flex justify-center">
			<!--      attention à ajouter une propriété pour etre sur que les évaluations sont non vides-->
			<Button type="submit" variant="destructive" class="mx-auto text-black bg-secondary hover:bg-secondary/90"
					v-if="evaluations && buttonsState.validate" @click="sendGrades" >
				Valider
			</Button>
			<Button type="submit" variant="destructive" v-if="buttonsState.loading" class="mx-auto text-black bg-secondary hover:bg-secondary/90">
				<Loader2 class="w-4 h-4 mr-2 animate-spin" />
				Veuillez patienter
			</Button>
		</div>
	</div>
</template>

<style scoped>

</style>