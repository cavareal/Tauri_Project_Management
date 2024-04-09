<script setup lang="ts">
import { PageTemplate } from "@/components/organisms/template"
import { StudentsTable, DeleteStudentsDialog } from "@/components/organisms/students"
import { NotAuthorized } from "@/components/organisms/errors"
import { Separator } from "@/components/ui/separator"
import { Row, Column } from "@/components/atoms/containers"
import { Button } from "@/components/ui/button"
import { GradeFactorsDialog } from "@/components/organisms/students"
import getCookie from "@/utils/cookiesUtils"
import DialogFile from "@/components/organisms/students/DialogFile.vue"
import { ref, watch } from "vue"
import type { Student } from "@/types/student"
import { getAllStudents } from "@/services/student-service"

const role = getCookie("role")
const hasPermission = role === "PL" || role === "OL"
const students = ref<Student[] | null>(null)

watch(() => { }, async() => {
	students.value = await getAllStudents()
}, { immediate: true })

</script>

<template>
	<PageTemplate>
		<Row class="items-center justify-between">
			<h1 class="text-3xl font-title-bold">Étudiants</h1>

			<Row class="gap-4" v-if="hasPermission">
				<DeleteStudentsDialog>
					<Button variant="outline">Supprimer les étudiants</Button>
				</DeleteStudentsDialog>
				<GradeFactorsDialog>
					<Button variant="outline">Modifier les coefficients</Button>
				</GradeFactorsDialog>
				<Button variant="default">Exporter</Button>
			</Row>
		</Row>

		<Separator />
		<Row v-if="students?.length === 0" class="items-center border border-gray-300 rounded-lg p-6">
			<Column class="items-start justify-center flex-1 gap-1">
				<h1 class="font-bold text-base">Vous n'avez pas encore importé les étudiants</h1>
				<p class="text-gray-400 text-sm">Pour importer les étudiants, il suffit de cliquer sur le bouton.</p>
			</Column>

			<DialogFile>
				<Button>
					Importer les étudiants
				</Button>
			</DialogFile>
		</Row>
		<StudentsTable v-else-if="hasPermission" />
		<NotAuthorized v-else />
	</PageTemplate>

</template>