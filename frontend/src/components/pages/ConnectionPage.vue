<script setup lang="ts">

import { RadioGroup } from "@/components/ui/radio-group"
import { Button } from "@/components/ui/button"
import { ref } from "vue"
import { Logo } from "@/components/atoms/logo"
import { redirect } from "@/utils/router"
import { Column } from "@/components/atoms/containers"
import Title from "@/components/atoms/texts/Title.vue"
import type { RoleType } from "@/types/role"
import { login } from "@/services/connection-service"
import { RadioLabel } from "@/components/molecules/radio"
import { CustomCard } from "@/components/molecules/card"
import { useQuery } from "@tanstack/vue-query"
import { useMutation } from "@tanstack/vue-query"

const selectedRole = ref<RoleType>("OPTION_LEADER")
const username = ref<string>("")
const password = ref<string>("")

const handleSubmit = async(event: Event) => {
  	event.preventDefault();
  	// TODO xss/escape html before ???
  	console.log(username.value, password.value)
	await mutate();
}

const { mutate, isPending, error } = useMutation({ mutationKey: ["login"], mutationFn: async() => {
	await login(username.value, password.value).then(() => {
		redirect("/");
	}).catch(() => {
		console.log("errorlogin")
	})
} })



const CARD_TITLE = "Bienvenue sur Tauri !"
const CARD_DESCRIPTION = "Sélectionnez vôtre rôle pour vous connecter. Cette page de connexion est temporaire."

</script>

<template>
	<Column class="bg-cover bg-white bg-center size-full items-center justify-start gap-12 pt-12 bg-eseo">
		<Column class="items-center gap-4">
			<Logo class="h-32 fill-dark-blue" />
			<Title class="text-dark-blue">Bienvenue sur Tauri !</Title>
		</Column>

		<CustomCard class="border-none drop-shadow-login-card" :title="CARD_TITLE" :description="CARD_DESCRIPTION">
			
			<form @submit.prevent="handleSubmit">
				<input type="text" id="username" name="username" v-model="username" placeholder="Addresse mail" required> 
				<input type="password" id="password" name="password" v-model="password" placeholder="Mot de passe" required> 
				<Button type="submit">Connexion</Button>
			</form> 
			
		</CustomCard>
	</Column>
</template>