<script setup lang="ts">

import Button from "../../ui/button/Button.vue"
import Input from "../../ui/input/Input.vue"
import { CloudUpload, Loader2 } from "lucide-vue-next"
import {
	Dialog,
	DialogContent,
	DialogDescription,
	DialogFooter,
	DialogHeader,
	DialogTitle,
	DialogTrigger,
	DialogClose
} from "@/components/ui/dialog"
import { reactive, ref } from "vue"

const fileName = ref("")
let file: File | null = null
function changeFile(event: Event) { // Type annotation for event parameter
	const inputElement = event.target as HTMLInputElement // Cast event.target to HTMLInputElement
	if (inputElement.files && inputElement.files[0]) {
		file = inputElement.files[0]
		fileName.value = file.name
	}
}

const state = reactive({
	loading: false
})

async function formSubmit() {
	if (!file) return

	let url = import.meta.env.VITE_TAURI_API_URL
	if (!url) return

	state.loading = true

	const formData = new FormData()
	formData.append("file-upload", file)
	await fetch(`${url}students/uploadCSV`, {
		method: "POST",
		body: formData
	})
		.then(() => location.reload())
		.catch((error) => console.error(error))
}
</script>


<template>
	<Dialog>
		<DialogTrigger>
			<slot />
		</DialogTrigger>

		<DialogContent>
			<DialogHeader>
				<DialogTitle class="text-lg font-semibold leading-7 text-slate-900 max-md:max-w-full">
					Importer les étudiants</DialogTitle>
				<DialogDescription class="mt-2 leading-5 text-slate-500 max-md:max-w-full">
					Pour importer les étudiants, il suffit de déposer le fichier qui contient
					toutes leurs informations ainsi que leurs notes.
				</DialogDescription>
			</DialogHeader>
			<label for="file-upload" class="flex flex-col justify-center p-4 mt-8 leading-5 text-center bg-white rounded-md
                border border-dashed text-slate-500 max-md:max-w-full">
				<label for="file-upload" class="flex flex-col justify-center px-20 py-5 max-md:px-5">
					<CloudUpload class="self-center w-12 aspect-square" />
					<div class="mt-1">
						Déposez un fichier ici ou cliquez ici pour sélectionnez un fichier
					</div>
				</label>
				<Input id="file-upload" type="file" @change="changeFile" />
			</label>

			<div class="mt-2 leading-[143%] text-slate-400 max-md:max-w-full">
				Format accepté : .csv
			</div>
			<DialogFooter class="space-x-2">
				<DialogClose>
					<Button variant="outline">
						Annuler
					</Button>
				</DialogClose>
				<Button type="submit" disabled class="flex items-center" v-if="state.loading">
					<Loader2 class="w-4 h-4 mr-2 animate-spin" />
					Chargement
				</Button>
				<Button type="submit" @click="formSubmit" v-else>
					Continuer
				</Button>
			</DialogFooter>
		</DialogContent>
	</Dialog>
</template>

<style scoped></style>