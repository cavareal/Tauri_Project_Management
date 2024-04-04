<script setup lang="ts">
import { Card, CardHeader, CardTitle, CardContent, CardFooter, CardDescription } from "../ui/card"
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"
import { ref } from "vue"
import { Logo } from "@/components/atoms/logo"

const selectedRole = ref("OL")

function formSubmit() {
	document.cookie = `role=${selectedRole.value}; path=/;`
	document.cookie = "token=bonamyRule34; path=/;"
}
</script>

<template>
	<div
		class="bg-cover bg-white bg-center size-full flex flex-col items-center justify-start gap-12 pt-12"
		style="background-image: url('/eseo.jpg')"
	>
		<div class="flex flex-col items-center gap-4">
			<Logo class="h-36 fill-dark-blue" />
			<h1 class="text-4xl font-title-medium text-dark-blue">Bienvenue sur Tauri !</h1>
		</div>
		<Card class="border-none drop-shadow-login-card">
			<form @submit="formSubmit" action="./">
				<CardHeader>
					<CardTitle>Connexion</CardTitle>
					<CardDescription>Sélectionnez vôtre rôle pour vous connecter. Cette page de connexion est temporaire.</CardDescription>
				</CardHeader>

				<CardContent>
					<RadioGroup class="flex justify-center flex-row" v-model="selectedRole">
						<div class="flex flex-col gap-2 w-1/2">
							<h4 class="mb-1 font-medium">Rôles professeurs</h4>
							<div class="flex items-center space-x-2">
								<RadioGroupItem name="role" id="OL" value="OL" />
								<Label for="OL">[OL] Option Leader</Label>
							</div>
							<div class="flex items-center space-x-2">
								<RadioGroupItem name="role" id="PL" value="PL" />
								<Label for="PL">[PL] Project Leader</Label>
							</div>
							<div class="flex items-center space-x-2">
								<RadioGroupItem name="role" id="SA" value="SA" />
								<Label for="SA">[SA] System Administrator</Label>
							</div>
							<div class="flex items-center space-x-2">
								<RadioGroupItem name="role" id="SS" value="SS" />
								<Label for="SS">[SS] Supervising Staff</Label>
							</div>
							<div class="flex items-center space-x-2">
								<RadioGroupItem name="role" id="TC" value="TC" />
								<Label for="TC">[TC] Technical Coaches</Label>
							</div>
						</div>

						<div class="flex flex-col gap-2 w-1/2">
							<h4 class="mb-1 font-medium">Rôles étudiants</h4>
							<div class="flex items-center space-x-2">
								<RadioGroupItem name="role" id="OS" value="OS" />
								<Label for="OS">[OS] Option Student</Label>
							</div>
							<div class="flex items-center space-x-2">
								<RadioGroupItem name="role" id="TM" value="TM" />
								<Label for="TM">[TM] Team Member</Label>
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