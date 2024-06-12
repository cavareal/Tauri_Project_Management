<script setup lang="ts">
import { ref, watch } from "vue"
import { Column, Row } from "@/components/atoms/containers"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { RoleTypeSchema, type RoleType, formatRole } from "@/types/role"
import { createUser } from "@/services/user/user.service"
import { createRole } from "@/services/role/role.service"
import { useMutation } from "@tanstack/vue-query"
import { createToast } from "@/utils/toast"
import { type CreateUser } from "@/types/user"
import { useForm } from "vee-validate"
import { toTypedSchema } from "@vee-validate/zod"
import * as z from "zod"

import {
	FormControl,
	FormField,
	FormItem,
	FormLabel,
	FormMessage
} from "@/components/ui/form"
import { Checkbox } from "@/components/ui/checkbox"
import { Subtitle } from "@/components/atoms/texts"
import { Label } from "@/components/ui/label"


const userRoles = ref<RoleType[]>([])
const userMail = ref<string>("")
const userName = ref<string>("")
const createdUser = ref<CreateUser>()

const emits = defineEmits(["add:user"])


watch(userMail, (newMail) => {
	if (newMail) {
		const [firstName, lastNameWithDomain] = newMail.split("@")[0].split(".")
		if (firstName && lastNameWithDomain) {
			const lastName = lastNameWithDomain.toUpperCase()
			const formattedFirstName = firstName.charAt(0).toUpperCase() + firstName.slice(1)
			userName.value = `${lastName} ${formattedFirstName}`
		} else {
			userName.value = ""
		}
	}
})


const { error, mutate } = useMutation({
	mutationKey: ["add-new-user"],
	mutationFn: async() => {

		if (!userMail.value || !userRoles.value || !userName.value) {
			alert("Veuillez remplir tous les champs")
			return
		}

		createdUser.value = { name: userName.value, email: userMail.value, password: "", privateKey: "" }

		await createUser(createdUser.value)
			.then(() => {
				createRole(userMail.value, userRoles.value)
					.then(() => {
						createToast("L'utilisateur a été ajouté avec son/ses rôle(s)")
						userMail.value = ""
						userName.value = ""
						userRoles.value = []
						emits("add:user")
					})
					.catch(() => createToast("Erreur lors de la création du/des rôle(s)"))
			})
			.catch(() => {
				createToast("Erreur lors de la création d'un utilisateur. Il se peut que l'email soit déjà enregistré.")
			})
	}
})


const formSchema = toTypedSchema(z.object({
	roles: z.array(z.string()).refine(value => value.some(role => role), {
		message: "Vous devez choisir au moins 1 role."
	})
}))

const { handleSubmit } = useForm({
	validationSchema: formSchema,
	initialValues: {
		roles: []
	}
})

const onSubmit = handleSubmit((values) => {
	userRoles.value = values.roles as RoleType[]
	mutate()
})

</script>

<template>
	<Column class="gap-4 w-1/2">
		<Subtitle>Ajouter un utilisateur</Subtitle>

		<Column class="w-full gap-2">
			<Row class="items-center gap-2">
				<Label for="email" class="w-1/3">Email</Label>
				<Input id="email"
					class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
					type="text" v-model="userMail" />
			</Row>

			<Row class="items-center gap-2">
				<Label for="username" class="w-1/3">Nom</Label>
				<Input id="username"
					class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
					type="text" v-model="userName" />
			</Row>
		</Column>

		<form @submit.prevent="onSubmit" class="flex flex-col gap-4">
			<FormField name="items">
				<FormItem>
					<div class="mb-4">
						<Label class="text-base">
							Rôle(s)
						</Label>
					</div>
					<FormField v-for="roleType in RoleTypeSchema.options" v-slot="{ value, handleChange }"
						:key="roleType" type="checkbox" :value="roleType" :unchecked-value="false" name="roles">
						<FormItem v-if="roleType != 'OPTION_STUDENT'"
							class="flex flex-row items-start space-x-3 space-y-0">
							<FormControl>
								<Checkbox :checked="value.includes(roleType)" @update:checked="handleChange" />
							</FormControl>
							<FormLabel class="font-normal">
								{{ formatRole(roleType) }}
							</FormLabel>
						</FormItem>
					</FormField>
					<FormMessage />
				</FormItem>
			</FormField>
			<Row class="justify-end">
				<Button class="w-44">Valider</Button>
			</Row>
			<p v-if=error>Erreur lors de la création d'un utilisateur</p>
		</form>
	</Column>
</template>