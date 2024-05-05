<script setup lang="ts">

import { SidebarTemplate } from "@/components/templates"
import { Cookies } from "@/utils/cookie"
import EditNbSprints from "@/components/organisms/sprints/EditNbSprints.vue"
import AddSprint from "@/components/organisms/sprints/AddSprint.vue"
import NotAuthorized from "@/components/organisms/errors/NotAuthorized.vue"
import { Header } from "@/components/molecules/header"

const token = Cookies.getToken()
const role = Cookies.getRole()

// TODO
// Check si déjà sprint en place
// Si oui, on affiche addSPrint avec une certaine phrase, et edit avec ceux actuel
// Sinon, on affiche seulement addSprint, avec une phrase différente

// TODO
// Add and edit : same or 2 different components ??

</script>

<template>
	<SidebarTemplate>
		<Header title="Sprints" />
		<!-- <EditNbSprints v-if="token && (role === 'PROJECT_LEADER' || role === 'OPTION_LEADER')" /> -->
		<AddSprint v-if="token && (role === 'PROJECT_LEADER' || role === 'OPTION_LEADER')" />
		<NotAuthorized v-else />
	</SidebarTemplate>
</template>