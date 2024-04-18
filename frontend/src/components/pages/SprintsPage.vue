<script setup lang="ts">
import { SidebarTemplate } from "@/components/templates"
import { getCookie } from "@/utils/cookie"
import EditNbSprints from "@/components/organisms/sprints/EditNbSprints.vue"
import NotAuthorized from "../organisms/errors/NotAuthorized.vue"

const token = getCookie("token")
const role = getCookie("role")
</script>

<template>
	<SidebarTemplate>
		<h1 class="text-3xl font-title-bold">Sprints</h1>
		<EditNbSprints v-if="token && (role === 'PROJECT_LEADER' || role === 'OPTION_LEADER')" />
		<NotAuthorized v-else />
	</SidebarTemplate>
</template>