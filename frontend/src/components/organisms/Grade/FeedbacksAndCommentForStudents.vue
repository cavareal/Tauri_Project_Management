<script setup lang="ts">
import IndividualComments from "@/components/organisms/Grade/IndividualComments.vue"
import { useQuery } from "@tanstack/vue-query"
import type { Feedback } from "@/types/feedback"
import { getIndividualsCommentsBySprintIdAndTeamId } from "@/services/feedback"
import { ref } from "vue"
import type { User } from "@/types/user"
import type { Student } from "@/types/student"
import { Column, Row } from "@/components/atoms/containers"
import { getStudentsByTeamId } from "@/services/student"
import { Subtitle, Text } from "@/components/atoms/texts"
import CommentsView from "@/components/molecules/rateContainer/CommentsView.vue"

const props = defineProps<{
  sprintId: string,
  teamId: string,
}>()

const commentsFiltered = ref<Feedback[]>([])
const students = ref<Student[]>([])

const { data: comments } = useQuery<Feedback[], Error>({
	queryKey: ["IndividualComments", props.teamId, props.sprintId],
	queryFn: async() => {
		const comments = await getIndividualsCommentsBySprintIdAndTeamId(props.teamId, props.sprintId)
		students.value = await getStudentsByTeamId(Number(props.teamId), true)
		commentsFiltered.value = comments.filter(comment => !comment.feedback)
		return comments
	}
})

const getStudentComments = (studentId: number) => {
	const studentComments = commentsFiltered.value.filter(comment => comment.student && comment.student.id === studentId)
	const authorsStudentComment = studentComments.map(comment => comment.author)
		.filter((author, index, self) => index === self.findIndex((t) => (
			t.id === author.id
		)))
	return { studentComments, authorsStudentComment }
}
</script>

<template>
  <Column class="bg-white border rounded-lg mt-4 p-2 w-full">
    <div v-for="student in students" :key="student.id" class="mb-3">
      <Subtitle>{{student.name}}</Subtitle>
      <Row class="w-full justify-center">
        <Column class="justify-center">
          <Text>Commentaires</Text>
          <CommentsView v-if="getStudentComments(student.id).studentComments.length > 0" :is-feedback="false" :comments="getStudentComments(student.id).studentComments" :authors="getStudentComments(student.id).authorsStudentComment"/>
          <p v-else>Pas de commentaire</p>
        </Column>
        <Column class="ml-3 justify-center" >
          <Text>Feedbacks</Text>
          <p>Pas de feedbacks</p>
        </Column>
      </Row>
    </div>
  </Column>
</template>

<style scoped>

</style>