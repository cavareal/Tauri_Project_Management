<script setup lang="ts">
import type { Feedback } from "@/types/feedback"
import { onMounted, ref, watch } from "vue"
import { getFeedbacksBySprintAndTeam } from "@/services/feedback-service"
import { Text } from "@/components/atoms/texts"
import { DialogClose } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { CustomDialog } from "@/components/molecules/dialog"
import type { User } from "@/types/user"
import { Separator } from "@/components/ui/separator"
import { useQuery } from "@tanstack/vue-query"

const props = defineProps({
	teamId: Number,
	sprintId: Number
})
const authorsFeedbacks = ref<User[]>([])

const { data: feedbacks } = useQuery<Feedback[], Error>({
	queryKey: ["feedbacks"],
	queryFn: () => getFeedbacksBySprintAndTeam(props.teamId!, props.sprintId!)
})

onMounted(() => {
	if (feedbacks.value) {
		authorsFeedbacks.value = feedbacks.value.map(feedback => feedback.author)
			.filter((author, index, self) => index === self.findIndex((t) => (
				t.id === author.id
			)))
	}
})

watch(() => feedbacks.value, () => {
	authorsFeedbacks.value = feedbacks.value!.map(feedback => feedback.author)
		.filter((author, index, self) => index === self.findIndex((t) => (
			t.id === author.id
		)))
})

const getFeedbacksFromAuthor = (authorId: string) => {
	if (feedbacks.value) {
		return feedbacks.value.filter(feedback => feedback.author.id.toString() === authorId)
	}
}

const refreshFeedbacks = async() => {
	feedbacks.value = await getFeedbacksBySprintAndTeam(props.teamId!, props.sprintId!)
}

watch(() => props.teamId, refreshFeedbacks)
watch(() => props.sprintId, refreshFeedbacks)

const DIALOG_TITLE = "Feedbacks"
const DIALOG_DESCRIPTION = "Feedbacks donnés à l'équipe durant le sprint"

</script>

<template>
  <CustomDialog :title="DIALOG_TITLE" :description="DIALOG_DESCRIPTION">
    <template #trigger>
      <slot />
    </template>
    <div v-if="!feedbacks && feedbacks!.length === 0">
      <Text class="text-center">Aucun feedback donné</Text>
    </div>
    <div v-else>
      <div v-for="author in authorsFeedbacks" :key="author.id"  class="p-10">
        <Text class="bold">{{ author.name }}</Text>
        <div v-for="feedback in getFeedbacksFromAuthor(author.id.toString())" :key="feedback.id" class="p-2">
          <Text>{{ feedback.content }}</Text>
          <Separator/>
        </div>
      </div>
    </div>

    <template #footer>
      <DialogClose>
        <Button variant="outline">Retour</Button>
      </DialogClose>
    </template>
  </CustomDialog>

</template>

<style scoped>

</style>