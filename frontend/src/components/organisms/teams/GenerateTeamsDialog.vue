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
const ratioGender = ref("20")

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
		await generateTeams(nbTeams.value, ratioGender.value)
		buttonsState.loading = false
		buttonsState.showGeneratedTeams = true
		buttonsState.generateTeams = true
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
				<div class="grid grid-cols-4 items-center gap-4">
					<Label for="nbTeams" class="text-right">
						Nombre d'équipes
					</Label>
					<Input id="nbTeams" type="number" v-model="nbTeams" class="col-span-3" />
				</div>
				<div class="grid grid-cols-4 items-center gap-4">
					<Label for="ratioGender" class="text-right">
						Ratio H/F (%)
					</Label>
					<Input id="ratioGender" type="number" v-model="ratioGender" class="col-span-3" />
				</div>
			</div>
			<DialogFooter>
				<Button type="submit" variant="destructive" v-if="buttonsState.generateTeams" @click="onClick"
					class="text-white bg-primary hover:bg-primary/90">
					Regénérer les équipes
				</Button>
				<Button type="submit" variant="destructive" v-if="buttonsState.loading" disabled
					class="flex items-center text-white bg-primary hover:bg-primary/90">
					<Loader2 class="w-4 h-4 mr-2 animate-spin" />
					Veuillez patienter
				</Button>
				<Button type="submit" variant="destructive" v-if="buttonsState.showGeneratedTeams"
					@click="showGeneratedTeams" class="text-white bg-primary hover:bg-primary/90">
					Voir les équipes générées
				</Button>
			</DialogFooter>
			<p v-if="errorMessage">{{ errorMessage }}</p>
		</DialogContent>
	</Dialog>
</template>