<script setup lang="ts">
import { useQuery } from "@tanstack/vue-query"
import type { Feedback } from "@/types/feedback"
import { getIndividualsCommentsBySprintIdAndTeamId } from "@/services/feedback"
import { Cookies } from "@/utils/cookie"
import { ref } from "vue"
import type { User } from "@/types/user"
import { Column, Row } from "@/components/atoms/containers"
import { MessageCircleReply } from "lucide-vue-next"
import { Subtitle, Text } from "@/components/atoms/texts"
import CommentsView from "@/components/molecules/rateContainer/CommentsView.vue"
import { getConnectedUser } from "@/services/user"

const props = defineProps<{
  sprintId: string,
  teamId: string,
}>()

const currentUserId = Cookies.getUserId()
const user = ref<User | null>(null)
const authors = ref<User[]>([])

const { data: studentComments } = useQuery<Feedback[], Error>({
	queryKey: ["IndividualComment", props.teamId, props.sprintId],
	queryFn: async() => {
		const comments = await getIndividualsCommentsBySprintIdAndTeamId(props.teamId, props.sprintId)
		user.value = await getConnectedUser()
		const studentComments = comments.filter(comment => comment.student && (comment.student.id === currentUserId))
		authors.value = studentComments.map(comment => comment.author)
			.filter((author, index, self) => index === self.findIndex((t) => (
				t.id === author.id
			)))
		return studentComments
	}
})

</script>

<template>
  <Column class="bg-white border rounded-lg mt-4 p-2 w-full">
    <Row class="mb-5">
      <MessageCircleReply class="mr-2" :size="40" :stroke-width="1"/>
      <Subtitle class="mb-4 text-center">Feedbacks Individuel</Subtitle>
    </Row>
      <Subtitle class="mb-5">{{user!.name}}</Subtitle>
      <Row class="w-full justify-center">
        <Column class="justify-center w-full">
          <Text>Feedbacks</Text>
          <CommentsView class="border rounded-lg" v-if="studentComments && studentComments.length > 0" :is-feedback="false" :comments="studentComments" :authors="authors"/>
          <Column v-else class="h-[300px] justify-center items-center border rounded-lg">
            <p>Pas de feedbacks</p>
          </Column>
        </Column>
      </Row>
  </Column>
</template>

<style scoped>

</style>