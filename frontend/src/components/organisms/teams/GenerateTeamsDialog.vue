<script setup lang="ts">
import { ref, reactive } from "vue"
import { Button } from "@/components/ui/button"
import {
	Dialog,
	DialogContent,
	DialogDescription,
	DialogFooter,
	DialogHeader,
	DialogTitle,
	DialogTrigger
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Loader2 } from "lucide-vue-next"
import { generateTeams } from "@/services/team-service"

// Définir les références pour les valeurs des inputs
const nbTeams = ref("6")
const womenPerTeam = ref("1")

// État pour contrôler l'affichage des boutons
const buttonsState = reactive({
	generateTeams: true,
	loading: false,
	showGeneratedTeams: false
})

// Message d'erreur
const errorMessage = ref("")

const onClick = async() => {
	buttonsState.generateTeams = false
	buttonsState.loading = true
	buttonsState.showGeneratedTeams = false

	try {
		await generateTeams(nbTeams.value, womenPerTeam.value)
		buttonsState.loading = false
		buttonsState.showGeneratedTeams = true
		buttonsState.generateTeams = false
	} catch (error) {
		console.error(error)
		errorMessage.value = "Erreur lors de la communication avec le serveur"
		buttonsState.loading = false
		buttonsState.generateTeams = true
	}
}

const showGeneratedTeams = () => {
	location.reload()
}
</script>

<template>
	<Dialog>
		<DialogTrigger>
			<slot />
		</DialogTrigger>

		<DialogContent class="sm:max-w-[600px]">
			<DialogHeader>
				<DialogTitle>Générer les équipes</DialogTitle>
				<DialogDescription>
					Modifiez les paramètres de génération, puis cliquez sur le bouton pour générer
					automatiquement les équipes.
				</DialogDescription>
			</DialogHeader>
			<div class="grid gap-4 py-4">
				<div class="grid grid-cols-5 items-center gap-4">
					<Label for="nbTeams" class="text-right col-span-2">
						Nombre d'équipes
					</Label>
					<Input id="nbTeams" type="number" v-model="nbTeams" class="col-span-3" />
				</div>
				<div class="grid grid-cols-5 items-center gap-4">
					<Label for="womenPerTeam" class="text-right col-span-2">
						Nombre de femmes par équipe
					</Label>
					<Input id="womenPerTeam" type="number" v-model="womenPerTeam" class="col-span-3" />
				</div>
			</div>
			<DialogFooter>
				<Button type="submit" v-if="buttonsState.generateTeams" @click="onClick">
					Générer les équipes
				</Button>
				<Button type="submit" v-if="buttonsState.loading" disabled
					class="flex items-center">
					<Loader2 class="w-4 h-4 mr-2 animate-spin" />
					Veuillez patienter
				</Button>
				<Button type="submit" v-if="buttonsState.showGeneratedTeams" @click="showGeneratedTeams">
					Voir les équipes générées
				</Button>
			</DialogFooter>
			<p v-if="errorMessage">{{ errorMessage }}</p>
		</DialogContent>
	</Dialog>
</template>