<script setup lang="ts">
import PageTemplate from "@/components/organisms/PageTemplate.vue"
import Logo from "../icons/Logo.vue"
import Button from "../ui/button/Button.vue"
</script>

<template>
	<PageTemplate>
		<div class="w-full h-screen flex flex-col items-center justify-center gap-6">
			<Logo class="h-64 fill-grey" />
			<div class="flex flex-col items-center justify-center">
				<h1 class="text-4xl font-title-medium">Page non trouvée</h1>
				<p class="text-md">La page que vous avez cherché n'existe pas...</p>
			</div>
			<RouterLink to="/">
				<Button class="text-white">Retour à l'accueil</Button>
			</RouterLink>
		</div>
	</PageTemplate>
</template>