<script setup lang="ts">

import { useForm } from "vee-validate"
import { toTypedSchema } from "@vee-validate/zod"
import { login } from "@/services/connection-service"

import { Button } from "@/components/ui/button"
import {
	FormControl,
	FormDescription,
	FormField,
	FormItem,
	FormLabel,
	FormMessage
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { toast } from "@/components/ui/toast"
import { AuthRequestSchema } from "@/types/auth-request"
import { redirect } from "@/utils/router"
import { CustomCard } from "@/components/molecules/card"

const formSchema = toTypedSchema(AuthRequestSchema)

const { handleSubmit } = useForm({
	validationSchema: formSchema
})

const onSubmit = handleSubmit(async(values) => {
	await login(values.login, values.password).then(() => {
		redirect("/")
	}).catch((e) => {
		console.log("errorlogin : " + e)
	})
})

const CARD_TITLE = "Connexion"
const CARD_DESCRIPTION = "Entrez votre adresse email et votre mot de passe pour vous connecter"

</script>

<template>
	<form class="space-y-4" @submit.prevent="onSubmit">
		<CustomCard class="border-none drop-shadow-login-card" :title="CARD_TITLE" :description="CARD_DESCRIPTION">

			<FormField v-slot="{ componentField }" name="login">
				<FormItem>
					<FormLabel>Addresse mail</FormLabel>
					<FormControl>
						<Input type="email" placeholder="email" v-bind="componentField" />
					</FormControl>

					<FormMessage />
				</FormItem>
			</FormField>

			<FormField v-slot="{ componentField }" name="password">
				<FormItem>
					<FormLabel>Mot de passe</FormLabel>
					<FormControl>
						<Input type="password" placeholder="mot de passe" v-bind="componentField" />
					</FormControl>

					<FormMessage />
				</FormItem>
			</FormField>

			<template v-slot:footer>
				<div class="flex justify-end">
					<Button type="submit">
						Connexion
					</Button>
				</div>
			</template>
		</CustomCard>
	</form>
</template>