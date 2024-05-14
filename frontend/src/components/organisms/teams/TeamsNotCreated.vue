<script setup lang="ts">

import { ActionSection } from "@/components/molecules/action-section"

const ACTION_TITLE = "Les équipes ne sont pas encore composées."
const ACTION_DESCRIPTION = "Veuillez patienter jusqu'à ce que les équipes soient composées par le leader du projet."

</script>

<template>
	<ActionSection :title="ACTION_TITLE" :description="ACTION_DESCRIPTION" />
</template>