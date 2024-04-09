<script setup lang="ts">
import { ref } from "vue"
import PageTemplate from "@/components/organisms/template/PageTemplate.vue"
import getCookie from "@/utils/cookiesUtils"
import EditNbSprints from "@/components/organisms/sprints/EditNbSprints.vue"
import NotAuthorized from "../organisms/errors/NotAuthorized.vue"

const token = getCookie("token")
const role = getCookie("role")
</script>

<template>
	<PageTemplate>
		<h1 class="text-3xl font-title-bold">Sprints</h1>
		<EditNbSprints v-if="token && role === 'PROJECT_LEADER'" />
		<NotAuthorized v-else />
	</PageTemplate>
</template>