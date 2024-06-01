<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Column } from "@/components/atoms/containers"
import AddProject from "./AddProject.vue"
import ProjectList from './ProjectList.vue'
import ProjectSelector from './ProjectSelector.vue'
import { getAllProjects } from "@/services/project/project.service"
import { type Project } from '@/types/project'

const projects = ref<Project[]>([])
const projectsLoading = ref(true)
const projectsError = ref(false)

onMounted(async () => {
  await refetch()
})

async function refetch() {
  projectsLoading.value = true
  projectsError.value = false
  try {
    projects.value = await getAllProjects()
  } catch (error) {
    projectsError.value = true
  } finally {
    projectsLoading.value = false
  }
}
</script>

<template>
  <div class="border border-gray-300 border-dashed rounded-lg flex justify-center flex-col items-stretch p-4 mb-10">
    <Column class="items-center gap-4">
      <p v-if="projectsLoading">Chargement...</p>
      <p v-else-if="projectsError">Erreur lors du chargement des projets.</p>
      <div v-else class="w-full">
        <ProjectSelector :projects="projects" />
        <ProjectList :projects="projects" @delete:project="refetch" />
        <AddProject @add:project="refetch" />
      </div>
    </Column>
  </div>
</template>
