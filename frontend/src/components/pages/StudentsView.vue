<script setup lang="ts">
import { PageTemplate } from "@/components/organisms/template"
import { StudentsTable } from "@/components/organisms/students"
import { Separator } from "@/components/ui/separator"
import { Row } from "@/components/atoms/containers"
import { Button } from "@/components/ui/button"
import { GradeFactorsDialog } from "@/components/organisms/students"
</script>

<template>
	<PageTemplate>
		<Row class="items-center justify-between">
			<h1 class="text-3xl font-title-bold">Étudiants</h1>

			<Row class="gap-4">
				<GradeFactorsDialog>
					<Button variant="outline">Modifier les coefficients</Button>
				</GradeFactorsDialog>
				<Button variant="default">Exporter</Button>
			</Row>
		</Row>

		<Separator />

		<StudentsTable />
	</PageTemplate>
</template>