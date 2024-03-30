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
import getCookie from "@/utils/cookiesUtils"
import { Loader2 } from 'lucide-vue-next'

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

// Méthode pour envoyer la requête POST
const generateTeams = async () => {
    buttonsState.generateTeams = false
    buttonsState.loading = true
    buttonsState.showGeneratedTeams = false

    const requestOptions = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: getCookie("token") || "null"
        },
        body: JSON.stringify({ nbTeams: nbTeams.value, ratioGender: ratioGender.value })
    }

    try {
        const response = await fetch(import.meta.env.VITE_TAURI_API_URL + "teams/create-teams", requestOptions)
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`)
        }
        buttonsState.loading = false
        buttonsState.showGeneratedTeams = true
        buttonsState.generateTeams = true
    } catch (error) {
        errorMessage.value = "Erreur lors de la communication avec le serveur"
        buttonsState.loading = false
        buttonsState.generateTeams = true
    }
}

const showGeneratedTeams = () => {
    console.log("ouai la teams")
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
                        <Button type="submit" variant="destructive" v-if="buttonsState.generateTeams" @click="generateTeams">
                            Générer les équipes
                        </Button>
                        <Button type="submit" variant="destructive" v-if="buttonsState.loading" disabled>
                            <Loader2 class="w-4 h-4 mr-2 animate-spin" />
                            Veuillez patienter
                        </Button>
                        <Button type="submit" variant="destructive" v-if="buttonsState.showGeneratedTeams" @click="showGeneratedTeams">
                            Voir les équipes générées
                        </Button>
                    </DialogFooter>
                    <p v-if="errorMessage">{{ errorMessage }}</p>
                </DialogContent>
            </Dialog>
        </div>
    </div>
</template>
