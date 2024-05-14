<script setup lang="ts">

import { EditSprintDialog, DeleteSprintDialog } from "."
import { formatSprintEndTypeWithDescription, type Sprint } from "@/types/sprint"
import { ActionSection } from "@/components/molecules/action-section"
import { Button } from "@/components/ui/button"
import { formatDate } from "@/utils/date"

const emits = defineEmits(["edit:sprint", "delete:sprint"])

const props = defineProps<{
    sprint: Sprint,
	canEditSprints: boolean
}>()

const TITLE = `Sprint ${props.sprint.sprintOrder} : du ${formatDate(props.sprint.startDate)} au ${formatDate(props.sprint.endDate)}`
const DESCRIPTION = formatSprintEndTypeWithDescription(props.sprint.endType)

</script>

<template>
    <ActionSection :title="TITLE" :description="DESCRIPTION">
        <EditSprintDialog v-if="canEditSprints" @edit:sprint="emits('edit:sprint')" :sprint="sprint">
            <Button variant="outline">Modifier</Button>
        </EditSprintDialog>

        <DeleteSprintDialog v-if="canEditSprints" @delete:sprint="emits('delete:sprint')" :sprintId="sprint.id" :sprintOrder="sprint.sprintOrder">
            <Button variant="outline">Supprimer</Button>
        </DeleteSprintDialog>
    </ActionSection>
</template>