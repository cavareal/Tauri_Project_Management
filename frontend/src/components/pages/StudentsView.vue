<script setup lang="ts">
import { PageTemplate } from "@/components/organisms/template"
import { StudentsTable, DeleteStudentsDialog } from "@/components/organisms/students"
import { NotAuthorized } from "@/components/organisms/errors"
import { Separator } from "@/components/ui/separator"
import { Row } from "@/components/atoms/containers"
import { Button } from "@/components/ui/button"
import { GradeFactorsDialog } from "@/components/organisms/students"
import getCookie from "@/utils/cookiesUtils"
import DialogFile from "@/components/organisms/DialogFile.vue"
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

		<div v-if="students?.length === 0" class="flex gap-0 justify-between p-6 mt-5 w-full bg-white rounded-md
				border border-solid border-slate-300 max-w-[1460px] max-md:flex-wrap max-md:px-5 max-md:max-w-full">
			<div class="flex flex-col flex-1 max-md:max-w-full">
				<div class="text-lg font-semibold leading-7 text-slate-900 max-md:max-w-full">
					Vous n'avez pas encore importé les étudiants
				</div>
				<div class="mt-2 text-sm leading-[143%] text-slate-500 max-md:max-w-full">
					Pour importer les étudiants, il suffit de cliquer sur le bouton.
				</div>
			</div>
			<DialogFile>
				<Button>
					Importer les étudiants
				</Button>
			</DialogFile>
		</div>
		<StudentsTable v-else-if="hasPermission" />
		<NotAuthorized v-else />
	</PageTemplate>

</template>