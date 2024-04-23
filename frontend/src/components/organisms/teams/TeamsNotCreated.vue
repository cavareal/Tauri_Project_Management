<script setup lang="ts">

import { ActionSection } from "@/components/molecules/action-section"

const ACTION_TITLE = "Il n'y a pas encore d'équipes"
const ACTION_DESCRIPTION = "Les équipes n'ont pas encore été crées par le project leader."

</script>

<template>
	<ActionSection :title="ACTION_TITLE" :description="ACTION_DESCRIPTION" />
</template>