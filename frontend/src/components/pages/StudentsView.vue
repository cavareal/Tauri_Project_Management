<script setup lang="ts">

import PageTemplate from "@/components/organisms/PageTemplate.vue"
import StudentsTable from "@/components/organisms/StudentsTable.vue"
import Button from "../ui/button/Button.vue"
import { ref } from "vue"
import Input from "@/components/ui/input/Input.vue"
import { CloudUpload } from "lucide-vue-next"
import getCookie from "@/utils/cookiesUtils"
import NotAutorized from "@/components/organisms/Teams/NotAuthorized.vue"

const popupVisible = ref(false)
const studentsImported = ref(false)
const token = getCookie("token")
const role = getCookie("role")
let file: File | null = null

function showPopup() {
	popupVisible.value = true
}

function changeFile(event: Event) { // Type annotation for event parameter
	const inputElement = event.target as HTMLInputElement // Cast event.target to HTMLInputElement
	if (inputElement.files && inputElement.files[0]) {
		file = inputElement.files[0]
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
			studentsImported.value = true
		} else {
			throw new Error("Erreur lors de la requête.")
		}
	} catch (error) {
		console.error("Erreur lors de l'envoi du formulaire :", error)
		// Gérer l'erreur ici
	} finally {
		popupVisible.value = false
	}
}

</script>

<template>
	<PageTemplate >
		<div class="flex flex-col items-center px-20 py-16 max-md:px-5">
			<div v-if="!studentsImported && (role === 'PL' || role === 'OL')">

				<div class="text-4xl font-medium text-black max-md:max-w-full">
					Étudiants
				</div>
				<div class="shrink-0 mt-5 max-w-full h-px border border-solid bg-slate-200 border-slate-200 w-[1140px]"></div>
				<div class="flex flex-col p-6 mt-5 w-full text-sm bg-white rounded-md border border-solid
				border-slate-300 max-w-[1140px] max-md:px-5 max-md:max-w-full">
					<div class="text-lg font-semibold leading-7 text-slate-900 max-md:max-w-full">
						Vous n’avez pas encore importé les étudiants
					</div>
					<div class="mt-2 leading-[143%] text-slate-500 max-md:max-w-full">
						Pour importer les étudiants, il suffit de cliquer sur le bouton
						ci-dessous.
					</div>
					<div class="justify-center self-center px-4 py-2 mt-4 font-medium leading-6 text-white bg-red-500 rounded-md">
						<Button class="w-5/6" @click="showPopup">Importer les étudiants</Button>
					</div>
				</div>
			</div>
      <NotAutorized v-else />


			<div v-if="popupVisible" class="z-1 flex flex-col self-center p-6 mt-5 max-w-full bg-white rounded-lg
			border border-solid border-slate-300 w-[487px] max-md:px-5">
				<div>
					<form @submit="formSubmit" action="./students">
						<div class="text-lg font-semibold leading-7 text-slate-900 max-md:max-w-full">
							Importer les étudiants
						</div>
						<div class="mt-2 leading-5 text-slate-500 max-md:max-w-full">
						Pour importer les étudiants, il suffit de déposer le fichier qui contient
						toutes leurs informations ainsi que leurs notes.
						</div>

						<div class="flex flex-col justify-center p-4 mt-8 leading-5 text-center bg-white rounded-md
						border border-dashed text-slate-500 max-md:max-w-full">
								<label for="file-upload" class="flex flex-col justify-center px-20 py-5 max-md:px-5">
									<CloudUpload class="self-center w-12 aspect-square"/>
									<div class="mt-1">
										Déposez un fichier ici ou cliquez ici pour sélectionnez un fichier
									</div>
								</label>
							<Input id="file-upload" type="file"  @change="changeFile"/>
						</div>

						<div class="mt-2 leading-[143%] text-slate-400 max-md:max-w-full">
							Format accepté : .csv
						</div>

						<div class="flex gap-5 pl-20 mt-8 font-medium leading-6 whitespace-nowrap max-md:flex-wrap max-md:pl-5">
							<Button class="justify-center px-4 py-2 bg-white rounded-md border border-solid border-slate-200 text-slate-900"
                      action="./students">Annuler</Button>
							<Button class="justify-center px-4 py-2 text-white bg-red-500 rounded-md" type="submit">Continuer</Button>
						</div>
					</form>
				</div>
			</div>

			<StudentsTable/>
		</div>
	</PageTemplate>

</template>