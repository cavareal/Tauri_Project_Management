<script setup lang="ts">
import { ContainerGradeType } from "@/components/molecules/rateContainer"
import { Button } from "@/components/ui/button"
import {
	Users,
	LucideCircleFadingPlus,
	User,
	SquareGanttChart,
	Play,
	Presentation,
	Blocks,
	LucideCirclePlus
} from "lucide-vue-next"
import { DialogRating, DialogBonus } from "@/components/organisms/rating"
import { hasPermission } from "@/services/user"
import { getTeamByUserId } from "@/services/team"
import { Cookies } from "@/utils/cookie"
import DialogIndividualRate from "@/components/organisms/rating/DialogIndividualRate.vue"
import { useQuery } from "@tanstack/vue-query"
import CommentContainer from "@/components/organisms/rating/CommentContainer.vue"
import { RatingBox } from "@/components/molecules/rating-box"
import { Column, Row } from "@/components/atoms/containers"
import { getAllRatedGradesFromConnectedUser } from "@/services/grade"
import { Loading } from "../loading"

const props = defineProps<{
	teamId: string,
	sprintId: string
}>()

// const currentUserId = Cookies.getUserId()
// const { data: currentUserTeam } = useQuery({ queryKey: ["team", currentUserId], queryFn: async() => getTeamByUserId(currentUserId) })

const { data: allGrades, isLoading, error } = useQuery({ queryKey: ["all-rated-grades"], queryFn: getAllRatedGradesFromConnectedUser })


const canGradeGlobalPerformance = hasPermission("GRADE_GLOBAL_PERFORMANCE")
const canGradeLimitedBonus = hasPermission("LIMITED_BONUS_MALUS")
const canGradeUnlimitedBonus = hasPermission("GIVE_UNLIMITED_BONUS_MALUS")
const canGradeMaterialSupport = hasPermission("GRADE_SUPPORT_MATERIAL")
const canGradeIndividualPerformance = hasPermission("GRADE_INDIVIDUAL_PERFORMANCE")
const canCommentIndividualPerformance = hasPermission("COMMENT_INDIVIDUAL_PERFORMANCE")
const canGradeTechnicalSolution = hasPermission("GRADE_TECHNICAL_SOLUTION")
const canGradeSprintConformity = hasPermission("GRADE_SPRINT_CONFORMITY")
const canGradeProjectManagement = hasPermission("GRADE_PROJECT_MANAGEMENT")
const canSeeFeedbacks = hasPermission("VIEW_FEEDBACK") && hasPermission("ADD_ALL_TEAMS_FEEDBACK")
const canSeePrivateComments = hasPermission("ADD_ALL_TEAMS_COMMENT") && hasPermission("VIEW_COMMENT")

</script>

<template>
	<Loading v-if="!allGrades" />

	<Column v-else class="items-stretch justify-start gap-4">
		<Row class="w-full items-stretch justify-stretch gap-4">
			<RatingBox class="flex-1" :all-grades="allGrades" grade-type-name="Performance globale de l'équipe"
				:sprint-id="props.sprintId" :team-id="props.teamId">
				<Users class="size-6 stroke-[1.33] text-dark-blue" />
			</RatingBox>

			<RatingBox class="flex-1" :all-grades="allGrades" grade-type-name="Conformité au sprint" :sprint-id="props.sprintId"
				:team-id="props.teamId">
				<Play class="size-6 stroke-[1.33] text-dark-blue" />
			</RatingBox>
		</Row>

		<Row class="w-full items-stretch justify-stretch gap-4">
			<RatingBox class="flex-1" :all-grades="allGrades" grade-type-name="Contenu de la présentation" :sprint-id="props.sprintId"
				:team-id="props.teamId">
				<Presentation class="size-6 stroke-[1.33] text-dark-blue" />
			</RatingBox>

			<RatingBox class="flex-1" :all-grades="allGrades" grade-type-name="Gestion de projet" :sprint-id="props.sprintId"
				:team-id="props.teamId">
				<SquareGanttChart class="size-6 stroke-[1.33] text-dark-blue" />
			</RatingBox>

			<RatingBox class="flex-1" :all-grades="allGrades" grade-type-name="Solution Technique" :sprint-id="props.sprintId"
				:team-id="props.teamId">
				<Blocks class="size-6 stroke-[1.33] text-dark-blue" />
			</RatingBox>
		</Row>
	</Column>

	<!-- <ContainerGradeType v-if="canGradeGlobalPerformance && currentUserTeam && currentUserTeam.id !== Number(props.teamId)" title="Note Globale de présentation" infotext="Vous devez évaluer chaque équipe sur sa présentation globale">
		<template #icon>
			<Users :size="40" :stroke-width="1"/>
		</template>
<template #dialog>
			<DialogRating title="Note de présentation" :description="`Veuillez noter la présentation de l équipe : ${props.teamId}`" :teamId="props.teamId" :sprintId="props.sprintId" gradeTypeString="Performance globale de l'équipe">
				<template #trigger>
					<Button variant="default">Noter une équipe</Button>
				</template>
</DialogRating>
</template>
</ContainerGradeType>

<ContainerGradeType v-if="canGradeTechnicalSolution && currentUserTeam && currentUserTeam.id === Number(props.teamId)"
	title="Solution Technique"
	infotext="Vous devez évaluer chaque équipe sur la solution technique qui a été mise en œuvre.">
	<template #icon>
			<Blocks :size="40" :stroke-width="1"/>
		</template>
	<template #dialog>
			<DialogRating title="Note de la solution technique" description="Veuillez noter la solution technique mise en œuvre par l'équipe" :teamId="props.teamId" :sprintId="props.sprintId" gradeTypeString="Solution Technique">
				<template #trigger>
					<Button variant="default">Noter une équipe</Button>
				</template>
	</DialogRating>
	</template>
</ContainerGradeType>

<ContainerGradeType v-if="canGradeSprintConformity && currentUserTeam && currentUserTeam.id === Number(props.teamId)"
	title="Conformité du sprint" infotext="Vous devez évaluer la conformité du sprint des équipes.">
	<template #icon>
			<Play :size="40" :stroke-width="1"/>
		</template>
	<template #dialog>
			<DialogRating title="Note de conformité du sprint" description="Veuillez noter la conformité du sprint" :teamId="props.teamId" :sprintId="props.sprintId" gradeTypeString="Conformité au sprint">
				<template #trigger>
					<Button variant="default">Noter une équipe</Button>
				</template>
	</DialogRating>
	</template>
</ContainerGradeType>

<ContainerGradeType v-if="canGradeProjectManagement && currentUserTeam && currentUserTeam.id === Number(props.teamId)"
	title="Gestion du projet" infotext="Vous devez évaluer chaque équipe sur sa gestion du projet.">
	<template #icon>
			<SquareGanttChart :size="40" :stroke-width="1"/>
		</template>
	<template #dialog>
			<DialogRating title="Note de la gestion du projet" description="Veuillez noter la gestion du projet" :teamId="props.teamId" :sprintId="props.sprintId" gradeTypeString="Gestion de projet">
				<template #trigger>
					<Button variant="default">Noter une équipe</Button>
				</template>
	</DialogRating>
	</template>
</ContainerGradeType>

<ContainerGradeType v-if="canGradeMaterialSupport" title="Support de présentation"
	infotext="Vous devez évaluer chaque équipe sur son support de présentation.">
	<template #icon>
			<Presentation :size="40" :stroke-width="1"/>
		</template>
	<template #dialog>
			<DialogRating title="Note du support de présentation" description="Veuillez noter le support de présentation" :teamId="props.teamId" :sprintId="props.sprintId" gradeTypeString="Support de présentation">
				<template #trigger>
					<Button variant="default">Noter une équipe</Button>
				</template>
	</DialogRating>
	</template>
</ContainerGradeType>

<ContainerGradeType v-if="canGradeIndividualPerformance || canCommentIndividualPerformance"
	title="Performance individuelle"
	infotext="Vous devez évaluer chaque étudiant sur sa performance individuelle lors de sa présentation.">
	<template #icon>
			<User :size="40" :stroke-width="1"/>
		</template>
	<template #dialog>
			<DialogIndividualRate title="Note de performance" description="Veuillez noter la performance individuelle de chaque étudiants" :teamId="props.teamId" :sprintId="props.sprintId" :canGrade="canGradeIndividualPerformance" :canComment="canCommentIndividualPerformance" gradeTypeString="Performance individuelle"></DialogIndividualRate>
		</template>
</ContainerGradeType>

<CommentContainer v-if="canSeeFeedbacks" title="Feedbacks"
	infoText="Vous pouvez donner un feedback sur les performances de l'équipe durant le sprint"
	:sprintId="props.sprintId" :teamId="props.teamId" :feedback="true" />
<CommentContainer v-if="canSeePrivateComments" title="Commentaires"
	info-text="Vous pouvez faire des commentaires sur les performances de l'équipe durant le sprint" :feedback="false"
	:sprintId="props.sprintId" :teamId="props.teamId" />

<ContainerGradeType
	v-if="(canGradeLimitedBonus || canGradeUnlimitedBonus) && currentUserTeam && currentUserTeam.id === Number(props.teamId)"
	title="Bonus et malus de mon équipe" infotext="Vous pouvez attribuer des bonus et des malus à votre équipe">
	<template #icon>
			<LucideCircleFadingPlus v-if="canGradeLimitedBonus && !canGradeUnlimitedBonus" :size="40" :stroke-width="1"/>
			<LucideCirclePlus v-if="canGradeUnlimitedBonus" :size="40" :stroke-width="1"/>
		</template>
	<template #dialog>
			<DialogBonus
				:limited="canGradeLimitedBonus && !canGradeUnlimitedBonus"
				:teamId="props.teamId"
				:sprintId="props.sprintId"
			></DialogBonus>
		</template>
</ContainerGradeType> -->
</template>