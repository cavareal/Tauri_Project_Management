<script setup lang="ts">

import { ActionSection } from "@/components/molecules/action-section"
import { RedirectButton } from "@/components/molecules/buttons"

const ACTION_TITLE = "Vous n'avez pas encore importé les étudiants"
const ACTION_DESCRIPTION = "Pour générer les équipes, il faut commencer par importer les étudiants."

</script>

<template>
	<ActionSection :title="ACTION_TITLE" :description="ACTION_DESCRIPTION">
		<RedirectButton link="/students">
			Importer les étudiants
		</RedirectButton>
	</ActionSection>
</template>