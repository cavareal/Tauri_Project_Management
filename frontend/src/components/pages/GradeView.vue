<script setup lang="ts">
import PageTemplate from "@/components/organisms/template/PageTemplate.vue"
import Tab from "@/components/molecules/tab/Tab.vue"
import Tabs from "@/components/molecules/tab/Tabs.vue"
import getCookie from "@/utils/cookiesUtils"
import NotAuthorized from "@/components/organisms/errors/NotAuthorized.vue"
import { defineComponent } from "vue"
import NotAutorized from "../organisms/errors/NotAuthorized.vue"
import TMOwnGradeView from "@/components/organisms/Grade/TMOwnGradeView.vue"


const token = getCookie("token")
const role = getCookie("role")

defineComponent({
	components: { PageTemplate, Tabs, Tab }
})
</script>

<template>
	<PageTemplate>
		<h1 class="text-3xl font-title-bold">Note</h1>
		<div class="tabs-example">
			<div class="example example-1">
				<NotAutorized v-if="!token || !role"/>
				<Tabs>
					<Tab title="Mes notes" >
						<TMOwnGradeView v-if="role === 'TEAM_MEMBER'"/>
						<NotAuthorized v-else/>
					</Tab>
					<Tab title="Mon équipe" v-if="role === 'TEAM_MEMBER' || role === 'SUPERVISING_STAFF'">
					</Tab>
					<Tab title="Equipes" v-if="role === 'TEAM_MEMBER' || role === 'SUPERVISING_STAFF' || role === 'PROJECT_LEADER'" >
					</Tab>
				</Tabs>
			</div>
		</div>
	</PageTemplate>
</template>