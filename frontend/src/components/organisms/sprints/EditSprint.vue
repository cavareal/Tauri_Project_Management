<script setup lang="ts">
import EditSprintDialog from "./EditSprintDialog.vue"
import type { Sprint } from "@/types/sprint"
import { ActionSection } from "@/components/molecules/action-section"
import { Button } from "@/components/ui/button"
import DeleteSprintDialog from "./DeleteSprintDialog.vue"

const emits = defineEmits(["edit:sprint", 'delete:sprint'])

const props = defineProps<{
    sprint: Sprint,
}>()


function formatDate(date: Date) {
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
}


</script>

<template>
    <ActionSection class="mb-5" :title="'Sprint ' + sprint.sprintOrder + ' : du ' + formatDate(sprint.startDate) + ' au ' + formatDate(sprint.endDate)" :description="sprint.endType">
        <EditSprintDialog @edit:sprint="emits('edit:sprint')" :sprint="sprint">
            <Button variant="outline" class="mx-1">Modifier</Button>
        </EditSprintDialog>

        <DeleteSprintDialog @delete:sprint="emits('delete:sprint')" :sprintId="sprint.id" :sprintOrder="sprint.sprintOrder">
            <Button variant="outline" class="mx-1">Supprimer</Button>
        </DeleteSprintDialog>
    </ActionSection>
</template>