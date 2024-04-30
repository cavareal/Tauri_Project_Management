<script setup lang="ts">

import { Button } from "@/components/ui/button"
import { CustomDialog, DialogClose } from "@/components/molecules/dialog"
import { ref } from "vue"
import { useMutation } from "@tanstack/vue-query"
import { ErrorText } from "@/components/atoms/texts"
import { LoadingButton } from "@/components/molecules/buttons"
import { Textarea } from "@/components/ui/textarea"

const emits = defineEmits(["signal:teams"])
const open = ref(false)

// eslint-disable-next-line @typescript-eslint/require-await
const { mutate, isPending, error } = useMutation({ mutationKey: ["delete-teams"], mutationFn: async() => {
	open.value = false
	emits("signal:teams")
} })

const DIALOG_TITLE = "Signaler l'équipe"
const DIALOG_DESCRIPTION = "Qu'avez vous à signaler ?"

</script>

<template>
  <CustomDialog :title="DIALOG_TITLE" :description="DIALOG_DESCRIPTION" v-model:open="open">
    <template #trigger>
      <slot />
    </template>

    <ErrorText v-if="error" class="mb-2">Une erreur est survenue.</ErrorText>
    <Textarea></Textarea>

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