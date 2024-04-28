<script setup lang="ts">
import { ref } from "vue"
import { SidebarTemplate } from "@/components/templates"
import NotAutorized from "@/components/organisms/errors/NotAuthorized.vue"
import TMRateView from "@/components/organisms/Rate/TMRateView.vue"
import { getCookie } from "@/utils/cookie"
import SSTCRateView from "@/components/organisms/Rate/SSTCRateView.vue"
import { Header } from "@/components/molecules/header"
import {
	Select,
	SelectContent,
	SelectGroup,
	SelectItem,
	SelectLabel,
	SelectTrigger,
	SelectValue
} from "@/components/ui/select"

const token = getCookie("token")
const role = getCookie("role")
let nbSprints = ref("3")

/* GET number of sprints of this project */
const requestOptionsStudents = {
	method: "GET",
	headers: {
		"Content-Type": "application/json",
		Authorization: token || "null"
	}
}
const fetchNumberSprints = async() => {
	try {
		const response = await fetch(import.meta.env.VITE_TAURI_API_URL + "projects/sprints-number", requestOptionsStudents)
		if (!response.ok) {
			throw new Error(`HTTP error! status: ${response.status}`)
		}
		nbSprints.value = await response.text()
	} catch (error) {
		console.error(error)
	}
}
void fetchNumberSprints()

</script>

<template>
	<SidebarTemplate>
		<Header title="Evaluation">
			<Select>
				<SelectTrigger class="w-[180px]">
					<SelectValue placeholder="Sprint par défaut" />
				</SelectTrigger>
				<SelectContent>
					<SelectGroup>
						<SelectItem value="1">Sprint 1</SelectItem>
						<SelectItem value="2">Sprint 2</SelectItem>
						<SelectItem value="3">Sprint 3</SelectItem>
					</SelectGroup>
				</SelectContent>
			</Select>
		</Header>
		<div>
			<NotAutorized v-if="!token || !role"/>
			<TMRateView v-else-if="role === 'TEAM_MEMBER'"/>
			<SSTCRateView v-else-if="role === 'SUPERVISING_STAFF' || role==='TECHNICAL_COACH'"/>
			<NotAutorized v-else/>
		</div>
	</SidebarTemplate>
</template>