<script setup lang="ts">

import { MessageSquareReply } from "lucide-vue-next"
import { Button } from "@/components/ui/button"
import DialogFeedback from "@/components/organisms/rating/DialogFeedback.vue"
import DialogViewFeedback from "@/components/organisms/rating/DialogViewFeedback.vue"
import { ContainerGradeType } from "@/components/molecules/rateContainer"
import { hasPermission } from "@/services/user-service"

const props = defineProps<{
  teamId: number,
  sprintId: number,
  title: string,
  infoText: string
}>()

const canAddFeedbacks = hasPermission("ADD_ALL_TEAMS_FEEDBACK")

</script>

<template>
  <ContainerGradeType :title="props.title" :infotext="props.infoText">
    <template #icon>
      <MessageSquareReply :size="40" :stroke-width="1"/>
    </template>
    <template #dialog>
      <DialogViewFeedback :teamId="props.teamId" :sprintId="props.sprintId">
        <Button variant="outline">Voir les feedbacks</Button>
      </DialogViewFeedback>
      <DialogFeedback v-if="canAddFeedbacks" :selectedTeamId="props.teamId" :selectedSprintId="props.sprintId">
        <Button variant="default">Donner un feedback</Button>
      </DialogFeedback>
    </template>
  </ContainerGradeType>
</template>

<style scoped>

</style>