<script setup lang="ts">
import { Column, Row } from "@/components/atoms/containers"
import { InfoText, Subtitle } from "@/components/atoms/texts"
import { MessageCircleMore, MessageSquareReply } from "lucide-vue-next"
import { useQuery } from "@tanstack/vue-query"
import type { Feedback } from "@/types/feedback"
import { getCommentsBySprintAndTeam } from "@/services/feedback-service"
import type { User } from "@/types/user"
import { ref } from "vue"
import CommentsView from "@/components/molecules/rateContainer/CommentsView.vue"
import { Input } from "@/components/ui/input"
import { SendHorizontal } from "lucide-vue-next"
import { Button } from "@/components/ui/button"

const props = defineProps<{
  isFeedback: boolean,
  teamId: number,
  sprintId: number
}>()

const commentsFiltered = ref<Feedback[]>([])
const authorsComments = ref<User[]>([])

const title = props.isFeedback ? "Feedback" : "Commentaire"
const infoText = props.isFeedback ? "Vous pouvez donner un feedback sur les performances de l'équipe durant le sprint" : "Vous pouvez faire des commentaires sur les performances de l'équipe durant le sprint"

const { data: comments, refetch: refetchFeedbacks } = useQuery<Feedback[], Error>({
	queryKey: ["comments", props.teamId, props.sprintId, props.isFeedback],
	queryFn: async() => {
		const comments = await getCommentsBySprintAndTeam(props.teamId, props.sprintId)
		commentsFiltered.value = comments.filter(comment => comment.feedback === props.isFeedback)
		authorsComments.value = commentsFiltered.value.map(comment => comment.author)
			.filter((author, index, self) => index === self.findIndex((t) => (
				t.id === author.id
			)))
		console.log(commentsFiltered.value)
		return comments
	}
})

</script>

<template>
  <Row class="border rounded-lg p-2 md:p-6 w-1/2">
    <Column class="w-1/2 pr-5 h-full flex justify-between">
      <div>
        <Row class="pb-5">
          <MessageSquareReply v-if="props.isFeedback" class="w-[20%]" :size="40" :stroke-width="1"/>
          <MessageCircleMore v-else class="w-[20%]" :size="40" :stroke-width="1"/>
          <Subtitle class="text-center w-[80%]">{{title}}</Subtitle>
        </Row>
      <InfoText>{{infoText}}</InfoText>
      </div>
      <Row>
        <Input placeholder="saissisez un commentaire" class="w-full mr-2"/>
        <Button variant="default" size="icon" class="rounded-full h-10 w-14">
          <SendHorizontal class="w-5 h-5"/>
        </Button>
      </Row>
    </Column>
    <div class="w-1/2">
      <div class="bg-background">
        <CommentsView :authors="authorsComments" :comments="commentsFiltered"/>
      </div>
    </div>
  </Row>
</template>

<style scoped>

</style>