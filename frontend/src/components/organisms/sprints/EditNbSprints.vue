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
import { getCookie } from "@/utils/cookie"
import { Loader2 } from "lucide-vue-next"

// Définir les références pour les valeurs des inputs
const nbSprints = ref("6")
const errorMessage = ref("")
const token = getCookie("token")
const currentProject = getCookie("currentProject")

// État pour contrôler l'affichage des boutons
const buttonsState = reactive({
	updateNbSprints: true,
	loading: false
})

/* GET number of sprints of this project */
const requestOptionsStudents = {
	method: "GET",
	headers: {
		"Content-Type": "application/json",
		Authorization: token || "null"
	}
}
const fetchNumberSprints = async() => {
	try {
		const response = await fetch(import.meta.env.VITE_TAURI_API_URL + "projects/sprints/" + currentProject, requestOptionsStudents)
		if (!response.ok) {
			throw new Error(`HTTP error! status: ${response.status}`)
		}
		const data = await response.text()
		nbSprints.value = data
	} catch (error) {
		console.error(error)
	}
}
fetchNumberSprints()

// Méthode pour envoyer la requête POST
const updateNbSprints = async() => {
	buttonsState.updateNbSprints = false
	buttonsState.loading = true
	errorMessage.value = ""

	const requestOptions = {
		method: "PUT",
		headers: {
			"Content-Type": "application/json",
			Authorization: getCookie("token") || "null"
		},
		body: JSON.stringify({ nbSprints: nbSprints.value })
	}

	try {
		const response = await fetch(import.meta.env.VITE_TAURI_API_URL + "projects/sprints/" + currentProject, requestOptions)
		if (!response.ok) {
			throw new Error(`HTTP error! status: ${response.status}`)
		}
		buttonsState.loading = false
		buttonsState.updateNbSprints = true
		errorMessage.value = "Valeur mise à jour !"
	} catch (error) {
		errorMessage.value = "Erreur lors de la communication avec le serveur"
		buttonsState.loading = false
		buttonsState.updateNbSprints = true
	}
}

</script>

<template>
    <div class="border border-gray-300 rounded-lg p-2 md:p-3 my-4">
        <!-- <h1 class="font-bold text-base">Gérer ici le nombre de sprint du projet actuel</h1> -->
        <div class="w-full pt-5 pb-2 px-0 flex justify-center">
            <Dialog>
                <DialogTrigger as-child>
                    <Button>
                        <span class="mx-5 my-0">Gestion du nombre de sprints</span>
                    </Button>
                </DialogTrigger>
                <DialogContent class="sm:max-w-[600px]">
                    <DialogHeader>
                        <DialogTitle>Gérer le nombre de sprints de ce projet</DialogTitle>
                        <DialogDescription>
                            Modifiez le paramètre, puis cliquez sur le bouton pour mettre à jour la valeur
                        </DialogDescription>
                    </DialogHeader>
                    <div class="grid gap-4 py-4">
                        <div class="grid grid-cols-4 items-center gap-4">
                            <Label for="nbSprints" class="text-right">
                                Nombre de sprints
                            </Label>
                            <Input id="nbSprints" type="number" v-model="nbSprints" class="col-span-3" />
                        </div>
                    </div>
                    <DialogFooter>
                        <Button type="submit" variant="destructive" v-if="buttonsState.updateNbSprints" @click="updateNbSprints" class="text-white bg-primary hover:bg-primary/90">
                            Mettre à jour
                        </Button>
                        <Button type="submit" variant="destructive" v-if="buttonsState.loading" disabled class="flex items-center text-white bg-primary hover:bg-primary/90">
                            <Loader2 class="w-4 h-4 mr-2 animate-spin" />
                            Veuillez patienter
                        </Button>
                    </DialogFooter>
                    <p v-if="errorMessage">{{ errorMessage }}</p>
                </DialogContent>
            </Dialog>
        </div>
    </div>
</template>