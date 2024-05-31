<script setup lang="ts">

import { SidebarTemplate } from "@/components/templates"
import { Header } from "@/components/molecules/header"
import { Column } from "@/components/atoms/containers"
import { hasPermission } from "@/services/user/user.service"
import { NotAuthorized } from "@/components/organisms/errors"
import AddUser from "./../organisms/project/AddUser.vue"
import ManageUser from "./../organisms/project/ManageUser.vue"

</script>

<template>
	<SidebarTemplate>
		<NotAuthorized v-if="!hasPermission('MANAGE_PROJECT')" />
		<Column v-else class="gap-4">
			<Header title="Gestion de projet">
			</Header>
            <Column>
                <AddUser v-if="hasPermission('ADD_USER')" />
				<ManageUser v-if="hasPermission('DELETE_USER')" />
            </Column>
		</Column>
	</SidebarTemplate>
</template>