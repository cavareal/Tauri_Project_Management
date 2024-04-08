<script setup lang="ts">
import { PageTemplate } from "@/components/organisms/template"
import { StudentsTable } from "@/components/organisms/students"
import { NotAuthorized } from "@/components/organisms/errors"
import { Separator } from "@/components/ui/separator"
import { Row } from "@/components/atoms/containers"
import { Button } from "@/components/ui/button"
import { GradeFactorsDialog } from "@/components/organisms/students"
import getCookie from "@/utils/cookiesUtils"
import DialogFile from "@/components/organisms/DialogFile.vue"
import { ref } from "vue"


const studentsImported = ref(false)
const role = getCookie("role")
const hasPermission = role === "PL" || role === "OL"
</script>

<template>
	<PageTemplate >
		<div class="flex flex-col items-center px-20 py-16 max-md:px-5">
			<div v-if="!studentsImported && hasPermission ">

        <Row class="items-center justify-between">
          <h1 class="text-3xl font-title-bold">Étudiants</h1>

          <Row class="gap-4" v-if="hasPermission">
            <GradeFactorsDialog>
              <Button variant="outline">Modifier les coefficients</Button>
            </GradeFactorsDialog>
            <Button variant="default">Exporter</Button>
          </Row>
        </Row>

        <Separator />

				<div class="shrink-0 mt-5 max-w-full h-px border border-solid bg-slate-200 border-slate-200 w-[1140px]"></div>
				<div class="flex gap-0 justify-between p-6 mt-5 w-full bg-white rounded-md
				border border-solid border-slate-300 max-w-[1460px] max-md:flex-wrap max-md:px-5 max-md:max-w-full">
          <div class="flex flex-col flex-1 max-md:max-w-full">
            <div class="text-lg font-semibold leading-7 text-slate-900 max-md:max-w-full">
              Vous n’avez pas encore importé les étudiants
            </div>
            <div class="mt-2 text-sm leading-[143%] text-slate-500 max-md:max-w-full">
              Pour importer les étudiants, il suffit de cliquer sur le bouton.
            </div>
          </div>
            <DialogFile>
              <Button  class="justify-center px-4 py-2 my-auto text-sm font-medium leading-6 text-white bg-red-500 rounded-md">
                Importer les étudiants</Button>
            </DialogFile>
				</div>
			</div>
      <StudentsTable v-if="hasPermission" />
      <NotAutorized v-else />
		</div>
	</PageTemplate>

</template>