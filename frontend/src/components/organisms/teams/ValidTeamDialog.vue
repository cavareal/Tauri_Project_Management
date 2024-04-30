<script setup lang="ts">

import { Button } from "@/components/ui/button"
import { CustomDialog, DialogClose } from "@/components/molecules/dialog"
import { ref } from "vue"
import { useMutation } from "@tanstack/vue-query"
import { ErrorText } from "@/components/atoms/texts"
import { LoadingButton } from "@/components/molecules/buttons"

const emits = defineEmits(["valid:teams"])
const open = ref(false)

// eslint-disable-next-line @typescript-eslint/require-await
const { mutate, isPending, error } = useMutation({ mutationKey: ["delete-teams"], mutationFn: async() => {
	open.value = false
	emits("valid:teams")
} })

const DIALOG_TITLE = "Valider la composition des équipes"
const DIALOG_DESCRIPTION = "Êtes-vous bien sûr de vouloir valider la composition des équipes ?"

</script>

<template>
  <CustomDialog :title="DIALOG_TITLE" :description="DIALOG_DESCRIPTION" v-model:open="open">
    <template #trigger>
      <slot />
    </template>

    <ErrorText v-if="error" class="mb-2">Une erreur est survenue.</ErrorText>

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