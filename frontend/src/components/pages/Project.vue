<script setup lang="ts">

import { ref } from "vue"
import { SidebarTemplate } from "@/components/templates"
import { Header } from "@/components/molecules/header"
import { Column } from "@/components/atoms/containers"
import { hasPermission } from "@/services/user/user.service"
import { NotAuthorized } from "@/components/organisms/errors"
// import AddUsers from "@/components/organisms/project/AddUsers.vue"


</script>

<template>
	<SidebarTemplate>
		<NotAuthorized v-if="!hasPermission('MANAGE_PROJECT')" />
		<Column v-else class="gap-4">
			<Header title="Gestion de projet">
			</Header>
            <Column>
                <!-- <AddUsers /> -->
            </Column>
		</Column>
	</SidebarTemplate>
</template>