<script setup lang="ts">
import { ref } from "vue"
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

// Définir les références pour les valeurs des inputs
const nbTeams = ref("6")
const ratioGender = ref("20")

// Méthode pour envoyer la requête POST
const generateTeams = async() => {
	console.log(nbTeams, ratioGender)


	const requestOptions = {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ nbTeams: nbTeams.value, ratioGender: ratioGender.value })
	}

	try {
		const response = await fetch(import.meta.env.BASE_URL + "/api/generate", requestOptions)
		if (!response.ok) {
			throw new Error(`HTTP error! status: ${response.status}`)
		}
		const data = await response.json()
		console.log(data) // Gérer la réponse ici
	} catch (error) {
		console.error("There was an error!", error)
	}
}
</script>

<template>
    <div class="border border-gray-300 rounded-lg p-2 md:p-3 my-4">
        <h1 class="font-bold text-base">Vous n'avez pas encore créer les équipes</h1>
        <p class="text-gray-400 text-sm">Maintenant que les étudiants sont importés, il vous suffit de générer les
            équipes automatiquement.</p>
        <div class="w-full pt-5 pb-2 px-0 flex justify-center">
            <Dialog>
                <DialogTrigger as-child>
                    <Button variant="destructive">
                        <span class="mx-5 my-0">Générer les équipes</span>
                    </Button>
                </DialogTrigger>
                <DialogContent class="sm:max-w-[425px]">
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
                            <Input id="nbTeams" v-model="nbTeams" class="col-span-3" />
                        </div>
                        <div class="grid grid-cols-4 items-center gap-4">
                            <Label for="ratioGender" class="text-right">
                                Ratio H/F (%)
                            </Label>
                            <Input id="ratioGender" v-model="ratioGender" class="col-span-3" />
                        </div>
                    </div>
                    <DialogFooter>
                        <Button type="submit" variant="destructive" @click="generateTeams">
                            Générer les équipes
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </div>
    </div>
</template>