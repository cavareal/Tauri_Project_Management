<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Button } from "@/components/ui/button"
import { createToast } from '@/utils/toast'
import { Cookies } from '@/utils/cookie/cookie.util'
import { Select, SelectTrigger, SelectValue, SelectContent, SelectItem } from '@/components/ui/select'
import { type Project } from '@/types/project'

const props = defineProps<{
    projects: Array<Project>
}>()

const selectedProjectId = ref<string | null>(null)

function handleSelectChange(value: string) {
    selectedProjectId.value = value
}

const handleValidate = () => {
    if (selectedProjectId.value !== null) {
        Cookies.setProjectId(Number(selectedProjectId.value))
        createToast(`Projet ${selectedProjectId.value} sélectionné`)
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
    <div class="w-full flex flex-col items-center mb-10">
        <h2 class="text-xl font-semibold text-center">Projet actuel</h2>
        <Select :model-value="selectedProjectId ?? undefined" @update:model-value="handleSelectChange">
            <SelectTrigger class="mt-4 p-2 border rounded">
                <SelectValue />
            </SelectTrigger>
            <SelectContent>
                <SelectItem v-for="project in projects" :key="project.id" :value="project.id.toString()">
                    Projet : {{ project.id }}
                </SelectItem>
            </SelectContent>
        </Select>
        <Button @click="handleValidate" class="mt-4 px-4 py-2 text-white rounded-lg">Valider le choix</Button>
    </div>
</template>
