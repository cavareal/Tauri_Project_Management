<script setup lang="ts">
import Logo from "@/components/icons/Logo.vue"
</script>

<template>
    <div class="w-full h-screen flex flex-col items-center justify-center gap-6">
		<Logo class="h-64 fill-grey" />
		<div class="flex flex-col items-center justify-center">
			<h1 class="text-4xl font-title-medium">Non autorisé</h1>
			<p class="text-md">Vous n'avez pas l'autorisation d'accéder à cette page.</p>
		</div>
		<RouterLink to="/">
			<Button class="text-white">Retour à l'accueil</Button>
		</RouterLink>
		</div>
</template>