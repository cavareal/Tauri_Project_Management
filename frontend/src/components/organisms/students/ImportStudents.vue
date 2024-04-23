<script setup lang="ts">

import { ActionSection } from "@/components/molecules/action-section"
import { UploadStudentsDialog } from "."
import { Button } from "@/components/ui/button"

const emits = defineEmits(["import:students"])

const ACTION_TITLE = "Vous n'avez pas encore importé les étudiants"
const ACTION_DESCRIPTION = "Pour importer les étudiants, il suffit de cliquer sur le bouton."

</script>

<template>
	<ActionSection :title="ACTION_TITLE" :description="ACTION_DESCRIPTION">
		<UploadStudentsDialog @import:students="emits('import:students')">
			<Button>Importer les étudiants</Button>
		</UploadStudentsDialog>
	</ActionSection>
</template>