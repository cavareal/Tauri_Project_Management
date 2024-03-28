<script setup lang="ts">

const handleFileUpload = (event: Event) => {
	const file = event.target.files[0] // Récupérer le premier fichier sélectionné
	if (!file) return // Vérifier si un fichier a été sélectionné

	const formData = new FormData()
	formData.append("file", file) // Ajouter le fichier au formulaire de données

	// Envoyer le fichier au backend
	fetch("/upload", {
		method: "POST",
		body: formData
	})
		.then(response => {
			// Vérifier si la réponse est OK
			if (!response.ok) {
				throw new Error("Erreur lors de l'envoi du fichier")
			}
			return response.json() // Si la réponse est OK, lire le JSON de la réponse
		})
		.then(data => {
			// Gérer la réponse du backend si nécessaire
			console.log("Réponse du serveur:", data)
		})
		.catch(error => {
			// Gérer les erreurs
			console.error("Erreur lors de l'envoi du fichier:", error)
		})
}

</script>

<template>
  <form @submit.prevent="submitForm" enctype="multipart/form-data">
    <p>
      <label for="fichier" class="block">{{ $t("accueil.importteams.form.file.label") }}</label>
      <input type="file" name="fichier" id="fichier" @change="handleFileUpload">
    </p>
    <input type="submit" value="Importer"/>
  </form>

  <form @submit.prevent="submitForm" enctype="multipart/form-data">
    <h3 class="text-lg font-bold mb-2">{{ $t("accueil.importteams.form.title") }}</h3>
    <p>
      <label for="fichier" class="block">{{ $t("accueil.importteams.form.file.label") }}</label>
      <input type="file" name="fichier" id="fichier" @change="handleFileChange" class="mt-1" />
    </p>
    <input type="hidden" name="objectif" value="fichier" />
    <input type="submit" :value="$t('accueil.importteams.form.action.label')" class="mt-4 bg-purple-500 text-white px-4 py-2 rounded hover:bg-purple-600 cursor-pointer" />
  </form>
</template>

<style scoped>

</style>