<script setup lang="ts">
import { ref } from "vue"
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
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"

const selectedTeam = ref("")
const note = ref("")
const evaluations = ref<Record<string, { team: string, note: number }[]>>({})

const props = defineProps({
	myProp: String,
	listTeam: Array
})

console.log(props.myProp)
console.log(props.listTeam)

function addEvaluation() {
	if (!evaluations.value[selectedTeam.value]) {
		evaluations.value[selectedTeam.value] = []
	}

	const teamIndex = evaluations.value[selectedTeam.value].findIndex(e => e.team === selectedTeam.value)
	if (teamIndex !== -1) {
		evaluations.value[selectedTeam.value][teamIndex].note = Number(note.value)
	} else {
		evaluations.value[selectedTeam.value].push({ team: selectedTeam.value, note: Number(note.value) })
	}
}

const sendGrade = async(evaluations : never) => {
	try {
		const response = await fetch("http://backend-url/endpoint", {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(evaluations)
		})

		if (!response.ok) {
			throw new Error(`HTTP error! status: ${response.status}`)
		}

		// eslint-disable-next-line @typescript-eslint/no-unsafe-return
		return await response.json()
	} catch (error) {
		console.error(error)
	}
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
                <option v-for="(teamName, index) in listTeam?.[0]" :key="index" :value="teamName">{{ teamName }}</option>
              </select>
            </div>
            <div class="grid grid-cols-3 items-center gap-4">
              <Label for="note">Note :</Label>
              <Input id="note" type="number" min="0" max="20" v-model="note" />
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

    <!-- Afficher les évaluations -->
    <div v-for="(teamEvaluations, teamName) in evaluations" :key="teamName">
      <h2>{{ teamName }}</h2>
      <ul>
        <li v-for="(evaluation, index) in teamEvaluations" :key="index">
          {{ evaluation.note }}
        </li>
      </ul>
    </div>
  </div>
</template>

<style scoped>

</style>