<script setup lang="ts">
import PageTemplate from "@/components/organisms/PageTemplate.vue"
import TeamsCreated from "@/components/organisms/Teams/TeamsCreated.vue"
import TeamsNotsCreated from "@/components/organisms/Teams/TeamsNotsCreated.vue"
import StudentsNotImported from "@/components/organisms/Teams/StudentsNotImported.vue"
import NotAutorized from "@/components/organisms/Teams/NotAuthorized.vue"

</script>

<template>
	<PageTemplate>
		<h1 class="text-3xl font-title-bold">Equipes</h1>
		<StudentsNotImported />
		<TeamsNotsCreated />
		<TeamsCreated />
		<NotAutorized />
	</PageTemplate>
</template>