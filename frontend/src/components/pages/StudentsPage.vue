<script setup lang="ts">

import { SidebarTemplate } from "@/components/templates"
import { StudentsTable, DeleteStudentsDialog, ImportStudents } from "@/components/organisms/students"
import { Error, NotAuthorized } from "@/components/organisms/errors"
import { Button } from "@/components/ui/button"
import { GradeFactorsDialog } from "@/components/organisms/students"
import { getCookie } from "@/utils/cookie"
import { getAllStudents } from "@/services/student-service"
import { Header } from "@/components/molecules/header"
import type { RoleType } from "@/types/role"
import { computed } from "vue"
import { getAllImportedGradeTypes } from "@/services/grade-type-service"
import { getAllGrades } from "@/services/grade-service"
import { useQuery } from "@tanstack/vue-query"

const role = getCookie<RoleType>("role")
const currentProjectId = getCookie("currentProject")
const hasPermission = role === "PROJECT_LEADER" || role === "OPTION_LEADER"

const { data: students, refetch: refetchStudents, error: studentsError } = useQuery({ queryKey: ["students"], queryFn: async() => (await (getAllStudents(currentProjectId))) })
const { data: gradeTypes, refetch: refetchGradeTypes, error: gradeTypesError } = useQuery(
	{ queryKey: ["gradeTypes"], queryFn: getAllImportedGradeTypes }
)
const { data: grades, refetch: refetchGrades, error: gradesError } = useQuery({ queryKey: ["grades"], queryFn: getAllGrades })

const error = computed(() => gradeTypesError.value || studentsError.value || gradesError.value)
const refetch = async() => {
	await refetchStudents()
	await refetchGradeTypes()
	await refetchGrades()
}

</script>

<template>
	<SidebarTemplate>
		<Header title="Étudiants">
			<DeleteStudentsDialog v-if="hasPermission && students && students?.length > 0" @delete:students="refetch">
				<Button variant="outline">Supprimer les étudiants</Button>
			</DeleteStudentsDialog>
			<GradeFactorsDialog v-if="hasPermission && gradeTypes" :grade-types="gradeTypes" @update:factors="refetch">
				<Button variant="outline">Modifier les coefficients</Button>
			</GradeFactorsDialog>
			<Button variant="default" v-if="hasPermission">Exporter</Button>
		</Header>

		<Error v-if="error" />
		<ImportStudents v-else-if="students?.length === 0 && hasPermission" @import:students="refetch" />
		<StudentsTable v-else-if="hasPermission" :students="students ?? null" :grade-types="gradeTypes ?? null" :grades="grades ?? null" />
		<NotAuthorized v-else />
	</SidebarTemplate>
</template>