<script setup lang="ts">

import { SidebarTemplate } from "@/components/templates"
import { getCookie } from "@/utils/cookie"
import EditNbSprints from "@/components/organisms/sprints/EditNbSprints.vue"
import AddSprint from "@/components/organisms/sprints/AddSprint.vue"
import NotAuthorized from "@/components/organisms/errors/NotAuthorized.vue"
import type { RoleType } from "@/types/role"
import { Header } from "@/components/molecules/header"

const token = getCookie("token")
const role = getCookie<RoleType>("role")

</script>

<template>
	<SidebarTemplate>
		<Header title="Sprints" />
		<EditNbSprints v-if="token && (role === 'PROJECT_LEADER' || role === 'OPTION_LEADER')" />
		<AddSprint v-if="token && (role === 'PROJECT_LEADER' || role === 'OPTION_LEADER')" />
		<NotAuthorized v-else />
	</SidebarTemplate>
</template>