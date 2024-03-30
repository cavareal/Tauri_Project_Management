<script setup lang="ts">
import { ref, onMounted } from "vue"
import { z } from "zod"
import { apiQuery } from "@/utils/api"

const ProjectSchema = z.object({
	id: z.number(),
	nbTeams: z.number(),
	ratioGender: z.number(),
	nbSprint: z.number(),
	phase: z.string()
})

type Project = z.infer<typeof ProjectSchema>

const data = ref<Project | null>()

const getProject = async(): Promise<Project | null> => {
	const response = await apiQuery({
		route: "projects/current",
		method: "GET",
		responseSchema: ProjectSchema
	})
	if (response.status === "error") {
		console.error(response.error)
		return null
	}
	return response.data
}

onMounted(async() => {
	const project = await getProject()
	if (project) {
		data.value = project
	}
})
</script>

<template>
	<div class="py-10 mx-auto">
		<Suspense>
			<h1 class="text-2xl font-bold text-black">Phase : {{ data?.phase }}</h1>
		</Suspense>
	</div>
</template>