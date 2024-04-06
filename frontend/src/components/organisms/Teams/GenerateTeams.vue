<script setup lang="ts">
import { Button } from "@/components/ui/button"
import { Row, Column } from "@/components/atoms/containers"
import { GenerateTeamsDialog } from "."

</script>

<template>
	<Row class="items-center border border-gray-300 rounded-lg p-6">
		<Column class="items-start justify-center flex-1 gap-1">
			<h1 class="font-bold text-base">Vous n'avez pas encore d'équipes</h1>
			<p class="text-gray-400 text-sm">Maintenant que les étudiants sont importés, il vous suffit de générer les
				équipes automatiquement.</p>
		</Column>

		<GenerateTeamsDialog>
			<Button>
				<span class="mx-5 my-0">Générer les équipes</span>
			</Button>
		</GenerateTeamsDialog>
	</Row>
</template>