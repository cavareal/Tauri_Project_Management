<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Button } from "@/components/ui/button"
import { createToast } from '@/utils/toast'
import { Cookies } from '@/utils/cookie/cookie.util'
import { Select, SelectTrigger, SelectValue, SelectContent, SelectItem } from '@/components/ui/select'
import { type Project } from '@/types/project'
import { setActualProject } from '@/services/project'
import Column from '@/components/atoms/containers/Column.vue'

const emits = defineEmits(["choose:project"])

const props = defineProps<{
    projects: Array<Project>
}>()

const selectedProjectId = ref<string | null>(null)

function handleSelectChange(value: string) {
    selectedProjectId.value = value
}

function getNameById(id: string | null): string {
    if(id === null) return ""
    const project = props.projects.find(project => project.id === Number(id))
    return project?.name ?? ""
}

const handleValidate = () => {
    if (selectedProjectId.value !== null) {
        setActualProject(Number(selectedProjectId.value))
            .then(() => {
                emits('choose:project')
                console.log()
                Cookies.setProjectId(Number(selectedProjectId.value))
                createToast("Projet "+ getNameById(selectedProjectId.value) +" sélectionné")
            })
    } else {
        createToast("Veuillez sélectionner un projet")
    }
}

onMounted(() => {
    const currentProjectId = Cookies.getProjectId()
    if (currentProjectId !== undefined && currentProjectId !== null) {
        selectedProjectId.value = currentProjectId.toString()
    }
})
</script>

<template>
    <Column class="mx-5">
        <h2 class="text-xl font-semibold text-center">Projet actuel</h2>
        <Select :model-value="selectedProjectId ?? undefined" @update:model-value="handleSelectChange">
            <SelectTrigger class="mt-4 p-2 border rounded">
                <SelectValue />
            </SelectTrigger>
            <SelectContent>
                <SelectItem v-for="project in projects" :key="project.id" :value="project.id.toString()">
                    {{ project.name }}
                </SelectItem>
            </SelectContent>
        </Select>
        <div class="flex justify-center">
            <Button @click="handleValidate" class="mt-4">Valider le choix</Button>
        </div>
    </Column>
</template>
