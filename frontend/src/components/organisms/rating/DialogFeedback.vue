<script setup lang="ts">

import { DialogClose } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { CustomDialog } from "@/components/molecules/dialog"
import { ErrorText, Text } from "@/components/atoms/texts"
import { LoadingButton } from "@/components/molecules/buttons"
import { Textarea } from "@/components/ui/textarea"
import { onMounted, ref, computed } from "vue"
import { useMutation } from "@tanstack/vue-query"
import { createToast } from "@/utils/toast"
import type { Team } from "@/types/team"
import { CustomSelect } from "@/components/molecules/select"
import { getTeams } from "@/services/team-service"
import { createFeedback } from "@/services/feedback-service"

const open = ref(false)
const feedback = ref("")
const teams = ref<Team[]>()
const selectedTeamId = ref<string>()

const isDisabled = computed(() => feedback.value === "" || selectedTeamId.value === undefined)

const { mutate, isPending, error } = useMutation({ mutationKey: ["signal-teams"], mutationFn: async() => {
	await createFeedback(selectedTeamId.value!, feedback.value)
		.then(() => open.value = false)
		.then(() => createToast("Le feedback a été enregistré."))
} })

const DIALOG_TITLE = "Donner un feedback"
const DIALOG_DESCRIPTION = "Envoyer un feedback sur le déroulement du dernier sprint"

onMounted(async() => {
	teams.value = await getTeams()
})


const getTeamName = (team : Team) => {
	if (team) {
		return team.name!
	}
	return ""
}

const getTeamID = (team : Team) => {
	if (team) {
		return team.id.toString()
	}
	return ""
}
</script>

<template>
  <CustomDialog :title="DIALOG_TITLE" :description="DIALOG_DESCRIPTION" v-model:open="open">
  <template #trigger>
    <slot />
  </template>

    <CustomSelect v-model="selectedTeamId" :getName="getTeamName" :data="teams!" :getId="getTeamID" placeholder="Sélectionner une équipe"/>
  <Text class="-mb-2">Votre feedback</Text>
  <Textarea v-model="feedback" placeholder="Ajouter un feedback" class="max-h-64"></Textarea>
  <ErrorText v-if="error" class="mt-2">Une erreur est survenue.</ErrorText>

  <template #footer>
    <DialogClose v-if="!isPending">
      <Button variant="outline">Annuler</Button>
    </DialogClose>
    <LoadingButton type="submit" @click="mutate" :loading="isPending" :disabled="isDisabled">
      Confirmer
    </LoadingButton>
  </template>
  </CustomDialog>
</template>

<style scoped>

</style>