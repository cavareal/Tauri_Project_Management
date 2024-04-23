<script setup lang="ts">

import { Button } from "@/components/ui/button"
import { ExternalLink } from "lucide-vue-next"
import { RouterLink } from "vue-router"
import { ActionSection } from "@/components/molecules/action-section"

const ACTION_TITLE = "Vous n'avez pas encore importé les étudiants"
const ACTION_DESCRIPTION = "Pour générer les équipes, il faut commencer par importer les étudiants."

</script>

<template>
	<ActionSection :title="ACTION_TITLE" :description="ACTION_DESCRIPTION">
		<RouterLink to="/students">
			<Button class="flex items-center">
				<ExternalLink class="w-4 h-4 mr-2" />
				Importer les étudiants
			</Button>
		</RouterLink>
	</ActionSection>
</template>