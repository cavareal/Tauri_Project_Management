<script setup lang="ts">
import { ref } from "vue"
import { useMutation } from "@tanstack/vue-query"
import { createToast } from "@/utils/toast"
import { Trash } from "lucide-vue-next"
import { deleteProject } from "@/services/project/project.service"
import { Button } from "@/components/ui/button"
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { type Project } from "@/types/project"
import { Column } from "@/components/atoms/containers"

defineProps<{
    projects: Array<Project>
}>()

const emits = defineEmits(["delete:project"])
const projectToDelete = ref<number | null>(null)
const isDialogOpen = ref<boolean>(false)

const openDelete = (projectId: number) => {
	projectToDelete.value = projectId
	isDialogOpen.value = true
}

const { mutate: deleteProjectMutate } = useMutation({
	mutationKey: ["delete-project"],
	mutationFn: async() => {
		if (projectToDelete.value !== null) {
			await deleteProject(projectToDelete.value)
				.then(() => {
					createToast("Le projet a été supprimé.")
					isDialogOpen.value = false
					emits("delete:project")
				})
				.catch(() => {
					createToast("Erreur lors de la suppression du projet.")
				})
		}
	}
})
</script>

<template>
    <Column class="mx-10">
        <h2 class="text-xl font-semibold text-center mb-4 mt-6">Liste des projets existants</h2>
        <div v-for="project in projects" :key="project.id" class="w-full flex justify-between items-center p-2 mb-5 border-b border-gray-500">
                <p class="font-medium">{{ project.name }}</p>
                <p class="text-gray-500">Nombre d'équipes : {{ project.nbTeams ? project.nbTeams : "pas encore générées" }}</p>
                <p class="text-gray-500">Phase : {{ project.phase }}</p>
                <p :class="project.actual ? 'text-green-500' : 'text-red-500'">
                    {{ project.actual ? 'Actuel' : 'Non Actuel' }}
                </p>
            <Dialog v-model:open="isDialogOpen" v-if="!project.actual">
                <DialogTrigger as-child>
                    <Button @click="openDelete(project.id)">
                        <Trash class="w-5 h-5" />
                    </Button>
                </DialogTrigger>
                <DialogContent class="sm:max-w-[425px]">
                    <DialogHeader>
                        <DialogTitle>Confirmer la suppression</DialogTitle>
                        <DialogDescription>
                            Êtes-vous sûr de vouloir supprimer ce projet ?
                        </DialogDescription>
                    </DialogHeader>
                    <DialogFooter>
                        <Button @click="isDialogOpen = false">Annuler</Button>
                        <Button class="bg-red-500 hover:bg-red-700 text-white" @click="deleteProjectMutate">
                            Supprimer
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </div>
    </Column>
</template>