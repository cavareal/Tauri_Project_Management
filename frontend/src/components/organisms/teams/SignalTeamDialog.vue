<script setup lang="ts">

import { Button } from "@/components/ui/button"
import { CustomDialog, DialogClose } from "@/components/molecules/dialog"
import { ref } from "vue"
import { useMutation } from "@tanstack/vue-query"
import { ErrorText } from "@/components/atoms/texts"
import { LoadingButton } from "@/components/molecules/buttons"
import { Textarea } from "@/components/ui/textarea"
import type { User } from "@/types/user"
import { getUserById } from "@/services/user-service"
import { createReportingFlag } from "@/services/flag-service"
import { createToast } from "@/utils/toast"

const props = defineProps<{
  currentUserId: string
}>()

const emits = defineEmits(["signal:teams"])
const open = ref(false)
const currentUser = ref<User>()
const textareaValue = ref("")

const { mutate, isPending, error } = useMutation({ mutationKey: ["signal-teams"], mutationFn: async() => {
	currentUser.value = await getUserById(props.currentUserId)
	let description = textareaValue.value !== "" ? textareaValue.value : "Signalement d'un problème dans la création des équipes"
	await createReportingFlag(currentUser.value, description)
		.then(() => open.value = false)
		.then(() => emits("signal:teams"))
		.then(() => createToast("Le signalement a été envoyé."))
} })

const DIALOG_TITLE = "Signaler la composition des équipes"
const DIALOG_DESCRIPTION = "Envoyez un signalement sur la composition des équipes au project leader."

</script>

<template>
  <CustomDialog :title="DIALOG_TITLE" :description="DIALOG_DESCRIPTION" v-model:open="open">
    <template #trigger>
      <slot />
    </template>

    <ErrorText v-if="error" class="mb-2">Une erreur est survenue.</ErrorText>
    <p>Votre commentaire</p>
    <Textarea v-model="textareaValue"></Textarea>

    <template #footer>
      <DialogClose v-if="!isPending">
        <Button variant="outline">Annuler</Button>
      </DialogClose>
      <LoadingButton type="submit" @click="mutate" :loading="isPending">
        Confirmer
      </LoadingButton>
    </template>
  </CustomDialog>
</template>