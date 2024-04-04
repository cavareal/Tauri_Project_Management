<script setup lang="ts">
import { Button } from "@/components/ui/button"
import { RouterLink } from "vue-router"
import { Logo } from "@/components/atoms/logo"

const props = defineProps<{
	title: string
	description: string
	logoClass?: string
	class?: string
}>()

</script>

<template>
	<div class="w-full h-screen flex flex-col items-center justify-center gap-6" :class="props.class">
		<Logo class="h-64" :class="props.logoClass" />

		<div class="flex flex-col items-center justify-center">
			<h1 class="text-4xl font-title-medium">{{ title }}</h1>
			<p class="text-md">{{ description }}</p>
		</div>

		<RouterLink to="/">
			<Button>Retour à l'accueil</Button>
		</RouterLink>
	</div>
</template>