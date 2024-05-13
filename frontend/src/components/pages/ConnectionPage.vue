<script setup lang="ts">

import { Logo } from "@/components/atoms/logo"
import { Column, Row } from "@/components/atoms/containers"
import Title from "@/components/atoms/texts/Title.vue"
import { Cookies } from "@/utils/cookie"
import { ConnectionForm } from "@/components/organisms/connection"

Cookies.removeAll()

</script>

<template>
	<!-- <Column class="bg-cover bg-white bg-center size-full items-center justify-start gap-12 pt-12 bg-eseo">
		<Column class="items-center gap-4">
			<Logo class="h-32 fill-dark-blue" />
			<Title class="text-dark-blue">Bienvenue sur Tauri !</Title>
		</Column>

		<ConnectionForm />
	</Column> -->
	<Row class="w-full h-full items-stretch justify-stretch">
		<img src="/background.png" alt="Background with Tauri Logo" class="w-1/2 h-full object-cover object-top" />

		<Column class="w-1/2 h-full items-center justify-center gap-4 bg-white">
			<Logo class="h-48 fill-dark-blue" />

			<Title class="text-dark-blue text-4xl">Bienvenue sur Tauri !</Title>

			<ConnectionForm />
		</Column>
	</Row>
</template>