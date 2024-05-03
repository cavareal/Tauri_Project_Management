<script setup lang="ts">

import { SidebarTemplate } from "@/components/templates"
import { getCookie } from "@/utils/cookie"
import EditNbSprints from "@/components/organisms/sprints/EditNbSprints.vue"
import AddSprint from "@/components/organisms/sprints/AddSprint.vue"
import { Error, NotAuthorized } from "@/components/organisms/errors"
import type { RoleType } from "@/types/role"
import { Header } from "@/components/molecules/header"
import { useQuery } from "@tanstack/vue-query"
import { getAllSprints } from "@/services/sprint-service"

const token = getCookie("token")
const role = getCookie<RoleType>("role")
// TODO
// Check si déjà sprint en place
// Si oui, on affiche addSPrint avec une certaine phrase, et edit avec ceux actuel
// Sinon, on affiche seulement addSprint, avec une phrase différente

// TODO
// Add and edit : same or 2 different components ??

const { data: sprints, error: error } = useQuery({
	queryKey: ["sprints"], queryFn: () => getAllSprints()
})


</script>

<template>
	<SidebarTemplate>
		<Header title="Sprints" />
		<p>{{ sprints }}</p>
		<Error v-if="error" />
		<EditNbSprints v-if="token && (role === 'PROJECT_LEADER' || role === 'OPTION_LEADER')" />
		<AddSprint v-if="token && (role === 'PROJECT_LEADER' || role === 'OPTION_LEADER')" />
		<NotAuthorized v-else />
	</SidebarTemplate>
</template>