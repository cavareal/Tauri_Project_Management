<script setup lang="ts">

import { ref } from "vue"
import { Logo } from "@/components/atoms/logo"
import { redirect } from "@/utils/router"
import { Column } from "@/components/atoms/containers"
import Title from "@/components/atoms/texts/Title.vue"
import { login } from "@/services/connection-service"
import { CustomCard } from "@/components/molecules/card"
import { useMutation } from "@tanstack/vue-query"
import { Cookies } from "@/utils/cookie"
import { ConnectionForm } from "@/components/molecules/connection"

Cookies.removeAll()

const username = ref<string>("")
const password = ref<string>("")

const handleSubmit = async(event: Event) => {
	event.preventDefault()
	// TODO xss/escape html before ???
	console.log(username.value, password.value)
	await mutate()
}

const { mutate, isPending, error } = useMutation({ mutationKey: ["login"], mutationFn: async() => {
	console.log("Page : " + username.value + " - " + password.value)
	await login(username.value, password.value).then(() => {
		redirect("/")
	}).catch((e) => {
		console.log("errorlogin : " + e)
	})
} })

</script>

<template>
	<Column class="bg-cover bg-white bg-center size-full items-center justify-start gap-12 pt-12 bg-eseo">
		<Column class="items-center gap-4">
			<Logo class="h-32 fill-dark-blue" />
			<Title class="text-dark-blue">Bienvenue sur Tauri !</Title>
		</Column>

		<ConnectionForm />
	</Column>
</template>