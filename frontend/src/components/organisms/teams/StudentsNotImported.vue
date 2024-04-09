<script setup lang="ts">
import { Button } from "@/components/ui/button"
import { ExternalLink } from "lucide-vue-next"
import { RouterLink } from "vue-router"
import { Row, Column } from "@/components/atoms/containers"

</script>

<template>
	<Row class="items-center border border-gray-300 rounded-lg p-6">
		<Column class="items-start justify-center flex-1 gap-1">
			<h1 class="font-bold text-base">Vous n'avez pas encore importé les étudiants</h1>
			<p class="text-gray-400 text-sm">Pour générer les équipes, il faut commencer par importer les étudiants.</p>
		</Column>

		<RouterLink to="/students">
			<Button class="flex items-center">
				<ExternalLink class="w-4 h-4 mr-2" />
				Importer les étudiants
			</Button>
		</RouterLink>
	</Row>
</template>