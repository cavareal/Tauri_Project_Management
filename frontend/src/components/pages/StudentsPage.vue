<script setup lang="ts">

import { SidebarTemplate } from "@/components/templates"
import { StudentsTable, DeleteStudentsDialog, UploadStudentsDialog } from "@/components/organisms/students"
import { Error, NotAuthorized } from "@/components/organisms/errors"
import { Button } from "@/components/ui/button"
import { GradeFactorsDialog } from "@/components/organisms/students"
import { getCookie } from "@/utils/cookie"
import { getAllStudents } from "@/services/student-service"
import { Header } from "@/components/molecules/header"
import type { RoleType } from "@/types/role"
import { useQuery } from "@/utils/api/api.util"
import { ActionSection } from "@/components/molecules/action-section"
import { computed } from "vue"
import { getAllImportedGradeTypes } from "@/services/grade-type-service"
import { getAllGrades } from "@/services/grade-service"

const role = getCookie<RoleType>("role")
const hasPermission = role === "PROJECT_LEADER" || role === "OPTION_LEADER"

const { data: students, refetch: refetchStudents, error: studentsError } = useQuery(getAllStudents)
const { data: gradeTypes, refetch: refetchGradeTypes, error: gradeTypesError } = useQuery(getAllImportedGradeTypes)
const { data: grades, refetch: refetchGrades, error: gradesError } = useQuery(getAllGrades)

const error = computed(() => gradeTypesError.value || studentsError.value || gradesError.value)
const refetch = async() => {
	await refetchStudents()
	await refetchGradeTypes()
	await refetchGrades()
}

const ACTION_TITLE = "Vous n'avez pas encore importé les étudiants"
const ACTION_DESCRIPTION = "Pour importer les étudiants, il suffit de cliquer sur le bouton."

</script>

<template>
	<SidebarTemplate>
		<Header title="Étudiants">
			<DeleteStudentsDialog v-if="hasPermission && students && students?.length > 0" @delete:students="refetch">
				<Button variant="outline">Supprimer les étudiants</Button>
			</DeleteStudentsDialog>
			<GradeFactorsDialog v-if="hasPermission" :grade-types="gradeTypes" @update:factors="refetch">
				<Button variant="outline">Modifier les coefficients</Button>
			</GradeFactorsDialog>
			<Button variant="default" v-if="hasPermission">Exporter</Button>
		</Header>

		<Error v-if="error" />
		<ActionSection v-else-if="students?.length === 0 && hasPermission" :title="ACTION_TITLE" :description="ACTION_DESCRIPTION">
			<UploadStudentsDialog @import:students="refetch">
				<Button>Importer les étudiants</Button>
			</UploadStudentsDialog>
		</ActionSection>

		<StudentsTable v-else-if="hasPermission" :students="students" :grade-types="gradeTypes" :grades="grades" />
		<NotAuthorized v-else />
	</SidebarTemplate>
</template>