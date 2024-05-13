<script setup lang="ts">

import { Logo } from "@/components/atoms/logo"
import { Column } from "@/components/atoms/containers"
import Title from "@/components/atoms/texts/Title.vue"
import { Cookies } from "@/utils/cookie"
import { ConnectionForm } from "@/components/molecules/connection"

Cookies.removeAll()

</script>

<template>
	<Column class="bg-cover bg-white bg-center size-full items-center justify-start gap-12 pt-12 bg-eseo">
		<Column class="items-center gap-4">
			<Logo class="h-32 fill-dark-blue" />
			<Title class="text-dark-blue">Bienvenue sur Tauri !</Title>
		</Column>

		<ConnectionForm />
	</Column>
</template>