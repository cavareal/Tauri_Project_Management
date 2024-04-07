<script setup lang="ts">
import { defineProps, ref } from "vue"
import { Ellipsis, Loader2, Pencil, Trash2 } from "lucide-vue-next"
import { Dialog, DialogTrigger, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter, DialogClose } from "@/components/ui/dialog"
import { Table, TableHeader, TableRow, TableHead, TableBody, TableCell } from "@/components/ui/table"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"
import {
	DropdownMenu,
	DropdownMenuContent,
	DropdownMenuItem,
	DropdownMenuTrigger
} from "@/components/ui/dropdown-menu"
import getCookie from "@/utils/cookiesUtils"

interface Team {
	name: string;
}

interface Evaluation {
	team: string;
	gradeContentPresentation: number;
	gradeMaterialSupport: number;
}

const token = getCookie("token")
const selectedTeam = ref("")
const contentPresentationNote = ref("")
const materialSupportNote = ref("")
const evaluations = ref<Record<string, Evaluation[]>>({})

const props = defineProps({
	listTeam: Array as () => Team[]
})
console.log(props.listTeam)

const buttonsState = ref({
	validate: true,
	loading: false
})

function redirect(): void {
	window.location.href = "/tauri/rating"
}

function addEvaluation() {
	if (!evaluations.value[selectedTeam.value]) {
		evaluations.value[selectedTeam.value] = []
	}

	evaluations.value[selectedTeam.value].push({
		team: selectedTeam.value,
		gradeContentPresentation: Number(contentPresentationNote.value),
		gradeMaterialSupport: Number(materialSupportNote.value)
	})
}

async function grades() {
	try {
		buttonsState.value.validate = false
		buttonsState.value.loading = true

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

		buttonsState.value.loading = false
		buttonsState.value.validate = true

		redirect()

		return await response.json()
	} catch (error) {
		buttonsState.value.loading = false
		buttonsState.value.validate = true

		console.error(error)
	}
}

function handleContentPresentationNoteInput(event: InputEvent) {
	const inputNote = parseInt((event.target as HTMLInputElement).value)
	if (inputNote > 20) {
		contentPresentationNote.value = String(20)
	} else {
		contentPresentationNote.value = String(inputNote)
	}
}

function handleMaterialSupportNoteInput(event: InputEvent) {
	const inputNote = parseInt((event.target as HTMLInputElement).value)
	if (inputNote > 20) {
		materialSupportNote.value = String(20)
	} else {
		materialSupportNote.value = String(inputNote)
	}
}

function sendGrades() {
	console.log(evaluations.value)
	void grades()
}
</script>


<template>
	<div class="border border-gray-300 rounded-lg p-2 md:p-3 my-4">
		<h1 class="font-bold text-base">Evaluer la performance d'une équipe</h1>
		<div class="w-full pt-5 pb-2 px-0 flex justify-center">
			<Dialog>
				<DialogTrigger as-child>
					<Button class="text-white bg-primary hover:bg-primary/90">
						<span class="mx-5 my-0">Evaluer</span>
					</Button>
				</DialogTrigger>
				<DialogContent class="sm:max-w-[500px]">
					<DialogHeader>
						<DialogTitle>Evaluer la performance</DialogTitle>
						<DialogDescription>
							Sélectionnez l'équipe à évaluer et indiquez les notes pour les critères de "Contenu de la présentation" et "Support matériel".
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
							<Label for="contenu-presentation">Contenu de la présentation :</Label>
							<Input id="contenu-presentation" type="number" min="0" max="20" v-model="contentPresentationNote" @input="handleContentPresentationNoteInput"/>
						</div>
						<div class="grid grid-cols-3 items-center gap-4">
							<Label for="support-materiel">Support matériel :</Label>
							<Input id="support-materiel" type="number" min="0" max="20" v-model="materialSupportNote" @input="handleMaterialSupportNoteInput"/>
						</div>
					</div>
					<DialogFooter>
						<DialogClose>
							<Button type="submit" variant="destructive" class="text-white bg-primary hover:bg-primary/90" @click="addEvaluation">
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
						<TableHead>Contenu de la présentation</TableHead>
						<TableHead>Support matériel</TableHead>
					</TableRow>
				</TableHeader>
				<TableBody>
					<TableRow v-for="(teamEvaluations, teamName) in evaluations" :key="teamName">
						<TableCell>
							{{ teamName }}
						</TableCell>
						<TableCell v-for="(evaluation, index) in teamEvaluations" :key="index">
							{{ evaluation.gradeContentPresentation }}
						</TableCell>
						<TableCell v-for="(evaluation, index) in teamEvaluations" :key="index">
							{{ evaluation.gradeMaterialSupport }}
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
			<Button type="submit" variant="destructive" class="mx-auto text-black bg-secondary hover:bg-secondary/90" v-if="evaluations && buttonsState.validate" @click="sendGrades">
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