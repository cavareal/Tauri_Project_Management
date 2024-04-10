<script setup lang="ts">
import { Card, CardHeader, CardTitle, CardContent, CardFooter, CardDescription } from "../ui/card"
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"
import { ref } from "vue"
import { Logo } from "@/components/atoms/logo"

import { UserSchema } from "@/types/user"
import { apiQuery } from "@/utils/api"

const selectedRole = ref("OPTION_LEADER")

async function formSubmit() {
	document.cookie = `role=${selectedRole.value}; path=/;`
	document.cookie = "token=bonamyRule34; path=/;"
	document.cookie = "user=52; path=/;"

	const response = await apiQuery({
		route: `roles/user/${selectedRole.value}`,
		responseSchema: UserSchema,
		method: "GET"
	})

	if (response.status === "error") {
		throw new Error(response.error)
	} else {
		document.cookie = `current_user=${response.data.id}; path=/;`
		window.location.href = "/"
	}
}
</script>

<template>
	<div
		class="bg-cover bg-white bg-center size-full flex flex-col items-center justify-start gap-12 pt-12"
		style="background-image: url('/eseo.jpg')"
	>
		<div class="flex flex-col items-center gap-4">
			<Logo class="h-36 fill-dark-blue"/>
			<h1 class="text-4xl font-title-medium text-dark-blue">Bienvenue sur Tauri !</h1>
		</div>
		<Card class="border-none drop-shadow-login-card">
			<form @submit.prevent="formSubmit">
				<CardHeader>
					<CardTitle>Connexion</CardTitle>
					<CardDescription>Sélectionnez vôtre rôle pour vous connecter. Cette page de connexion est
						temporaire.
					</CardDescription>
				</CardHeader>

				<CardContent>
					<RadioGroup class="flex justify-center flex-row" v-model="selectedRole">
						<div class="flex flex-col gap-2 w-1/2">
							<h4 class="mb-1 font-medium">Rôles professeurs</h4>
							<div class="flex items-center space-x-2">
								<RadioGroupItem name="role" id="OPTION_LEADER" value="OPTION_LEADER"/>
								<Label for="OPTION_LEADER">[OL] Option Leader</Label>
							</div>
							<div class="flex items-center space-x-2">
								<RadioGroupItem name="role" id="PROJECT_LEADER" value="PROJECT_LEADER"/>
								<Label for="PROJECT_LEADER">[PL] Project Leader</Label>
							</div>
							<div class="flex items-center space-x-2">
								<RadioGroupItem name="role" id="SYSTEM_ADMINISTRATOR" value="SYSTEM_ADMINISTRATOR"/>
								<Label for="SYSTEM_ADMINISTRATOR">[SA] System Administrator</Label>
							</div>
							<div class="flex items-center space-x-2">
								<RadioGroupItem name="role" id="SUPERVISING_STAFF" value="SUPERVISING_STAFF"/>
								<Label for="SUPERVISING_STAFF">[SS] Supervising Staff</Label>
							</div>
							<div class="flex items-center space-x-2">
								<RadioGroupItem name="role" id="TECHNICAL_COACH" value="TECHNICAL_COACH"/>
								<Label for="TECHNICAL_COACH">[TC] Technical Coach</Label>
							</div>
						</div>

						<div class="flex flex-col gap-2 w-1/2">
							<h4 class="mb-1 font-medium">Rôles étudiants</h4>
							<div class="flex items-center space-x-2">
								<RadioGroupItem name="role" id="OPTION_STUDENT" value="OPTION_STUDENT"/>
								<Label for="OPTION_STUDENT">[OS] Option Student</Label>
							</div>
							<div class="flex items-center space-x-2">
								<RadioGroupItem name="role" id="TEAM_MEMBER" value="TEAM_MEMBER"/>
								<Label for="TEAM_MEMBER">[TM] Team Member</Label>
							</div>
						</div>
					</RadioGroup>
				</CardContent>
				<CardFooter class="flex justify-end">
					<Button type="submit">Connexion</Button>
				</CardFooter>
			</form>
		</Card>
	</div>
</template>