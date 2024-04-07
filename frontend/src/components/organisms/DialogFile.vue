<script setup lang="ts">

import Button from "../ui/button/Button.vue"
import Input from "../ui/input/Input.vue"
import { CloudUpload } from "lucide-vue-next"
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
import { ref } from "vue"

const dropContainer = document.getElementById("dropcontainer")
const fileName = ref("")
let file: File | null = null
function changeFile(event: Event) { // Type annotation for event parameter
	const inputElement = event.target as HTMLInputElement // Cast event.target to HTMLInputElement
	if (inputElement.files && inputElement.files[0]) {
		file = inputElement.files[0]
		fileName.value = file.name
	}
}

async function formSubmit() {
	try {
		const formData = new FormData()
		/*const fileInput = document.getElementById("file-upload") as HTMLInputElement
      if (fileInput.files && fileInput.files[0]) {
      formData.append("file-upload", fileInput.files[0])*/
		formData.append("file-upload", file)
		const response = await fetch("http://localhost:8882/api/students/uploadTest", {
			method: "POST",
			body: formData
		})
		if (response.ok) {
			console.log("file uploaded successfully")
		} else {
			throw new Error("Erreur lors de la requête.")
		}
	} catch (error) {
		console.error("Erreur lors de l'envoi du formulaire :", error)
		// Gérer l'erreur ici
	}
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
          <CloudUpload class="self-center w-12 aspect-square"/>
          <div class="mt-1">
            Déposez un fichier ici ou cliquez ici pour sélectionnez un fichier
          </div>
        </label>
        <Input id="file-upload" type="file"  @change="changeFile"/>
      </label>

      <div class="mt-2 leading-[143%] text-slate-400 max-md:max-w-full">
        Format accepté : .csv
      </div>
      <DialogFooter class="flex gap-5 pl-20 mt-8 font-medium leading-6 whitespace-nowrap max-md:flex-wrap max-md:pl-5">
        <DialogClose>
          <Button class="justify-center px-4 py-2 bg-white rounded-md border border-solid border-slate-200 text-slate-900">
            Annuler</Button>
          <Button class="justify-center px-4 py-2 text-white bg-red-500 rounded-md" type="submit" @click="formSubmit">
            Continuer
          </Button>
        </DialogClose>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>

<style scoped>

</style>