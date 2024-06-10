<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Column } from "@/components/atoms/containers"
import ProjectAdd from "./ProjectAdd.vue"
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
    <Column class="items-center border rounded-md bg-white">
      <p v-if="projectsLoading">Chargement...</p>
      <p v-else-if="projectsError">Erreur lors du chargement des projets.</p>
      <div v-else class="w-full">
        <ProjectSelector :projects="projects" @choose:project="refetch" />
        <ProjectList :projects="projects" @delete:project="refetch" />
        <ProjectAdd @add:project="refetch" />
      </div>
    </Column>
</template>
