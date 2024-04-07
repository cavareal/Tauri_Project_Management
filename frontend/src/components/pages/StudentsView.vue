<script setup lang="ts">

import DialogFile from "@/components/organisms/DialogFile.vue"
import PageTemplate from "@/components/organisms/PageTemplate.vue"
import StudentsTable from "@/components/organisms/StudentsTable.vue"
import Button from "../ui/button/Button.vue"
import { ref } from "vue"
import getCookie from "@/utils/cookiesUtils"
import NotAutorized from "@/components/organisms/Teams/NotAuthorized.vue"

const studentsImported = ref(false)
const role = getCookie("role")
let file: File | null = null
</script>

<template>
	<PageTemplate >
		<div class="flex flex-col items-center px-20 py-16 max-md:px-5">
			<div v-if="!studentsImported && (role === 'PL' || role === 'OL')">

				<div class="text-4xl font-medium text-black max-md:max-w-full">
					Étudiants
				</div>
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
      <NotAutorized v-else />
			<StudentsTable/>
		</div>
	</PageTemplate>

</template>