<script setup lang="ts">

import { RadioGroup } from "@/components/ui/radio-group"
import { ref } from "vue"
import { Logo } from "@/components/atoms/logo"
import { redirect } from "@/utils/router"
import { Column } from "@/components/atoms/containers"
import Title from "@/components/atoms/texts/Title.vue"
import type { RoleType } from "@/types/role"
import { login } from "@/services/connection-service"
import { RadioLabel } from "@/components/molecules/radio"
import { CustomCard } from "@/components/molecules/card"
import { useMutation } from "@tanstack/vue-query"
import { LoadingButton } from "@/components/molecules/buttons"

const selectedRole = ref<RoleType>("OPTION_LEADER")

const { mutate, isPending, isSuccess } = useMutation({ mutationKey: ["login"], mutationFn: async() => {
	await login(selectedRole.value)
		.then(() => redirect("/"))
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
			<RadioGroup class="flex justify-center flex-row" v-model="selectedRole">
				<Column class="gap-2 w-1/2">
					<h4 class="mb-1 font-medium">Rôles professeurs</h4>
					<RadioLabel name="role" value="OPTION_LEADER">[OL] Option Leader</RadioLabel>
					<RadioLabel name="role" value="PROJECT_LEADER">[PL] Project Leader</RadioLabel>
					<RadioLabel name="role" value="SYSTEM_ADMINISTRATOR">[SA] System Administrator</RadioLabel>
					<RadioLabel name="role" value="SUPERVISING_STAFF">[SS] Supervising Staff</RadioLabel>
					<RadioLabel name="role" value="TECHNICAL_COACH">[TC] Technical Coach</RadioLabel>
				</Column>

				<Column class="gap-2 w-1/2">
					<h4 class="mb-1 font-medium">Rôles étudiants</h4>
					<RadioLabel name="role" value="OPTION_STUDENT">[OS] Option Student</RadioLabel>
					<RadioLabel name="role" value="TEAM_MEMBER">[TM] Team Member</RadioLabel>
				</Column>
			</RadioGroup>

			<template #footer>
				<LoadingButton @click="mutate" :loading="isPending || isSuccess">Connexion</LoadingButton>
			</template>
		</CustomCard>
	</Column>
</template>