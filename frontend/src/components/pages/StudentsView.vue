<script setup lang="ts">
import PageTemplate from "@/components/organisms/PageTemplate.vue"
import StudentsTable from "@/components/organisms/StudentsTable.vue"
import { Separator } from "@/components/ui/separator"
import { Row } from "@/components/atoms/containers"
import { Button } from "@/components/ui/button"
</script>

<template>
	<PageTemplate>
		<Row class="items-center justify-between">
			<h1 class="text-3xl font-title-bold">Étudiants</h1>
			<Row class="gap-4">
				<Button variant="outline">Ajouter un étudiant</Button>
				<Button variant="outline">Modifier les coefficients</Button>
				<Button variant="default">Exporter</Button>
			</Row>
		</Row>
		<Separator />
		<StudentsTable />
	</PageTemplate>
</template>