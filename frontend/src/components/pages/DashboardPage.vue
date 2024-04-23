<script setup lang="ts">

import { SidebarTemplate } from "@/components/templates"
import { ref } from "vue"
import { getCookie } from "@/utils/cookie"
import { Header } from "@/components/molecules/header"
import { Text } from "@/components/atoms/texts"
import { formatRole } from "@/types/role"

const message = ref("")

const role = getCookie("role") || "Aucun rôle"
message.value = formatRole(role)

</script>

<template>
	<SidebarTemplate>
		<Header title="Bonjour" />
		<Text>Votre rôle est : {{ message }}.</Text>
	</SidebarTemplate>
</template>