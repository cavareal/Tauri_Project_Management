<script setup lang="ts">
import { Card, CardHeader, CardTitle, CardContent, CardFooter, CardDescription } from "../ui/card"
import { RadioGroup, RadioGroupItem } from "../ui/radio-group"
import { Label } from "../ui/label"
import { Button } from "../ui/button"
import { ref } from "vue"

const selectedRole = ref("OL")

function formSubmit() {
	document.cookie = `role=${selectedRole.value}; path=/;`
}
</script>

<template>
	<div
		class="bg-cover bg-white bg-center size-full flex items-center justify-center"
		style="background-image: url('http://localhost:5173/bg_connection.svg')"
	>
		<Card class="w-1/3 h-auto">
			<form @submit="formSubmit" action="./">
				<CardHeader>
					<CardTitle>Sélection du rôle</CardTitle>
					<CardDescription>Ceci est une fenêtre de connexion temporaire</CardDescription>
				</CardHeader>

				<CardContent>
					<RadioGroup class="flex justify-center flex-row size-full" v-model="selectedRole">
						<div class="flex flex-col w-1/2">
							<h4>Rôles professeurs :</h4>
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

						<div class="flex flex-col w-1/2">
							<h4>Rôles étudiants :</h4>
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
				<CardFooter class="flex justify-center">
					<Button class="w-5/6" type="submit">Connexion</Button>
				</CardFooter>
			</form>
		</Card>
	</div>
</template>