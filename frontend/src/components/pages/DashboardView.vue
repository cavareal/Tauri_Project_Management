<script setup lang="ts">
import PageTemplate from "@/components/organisms/template/PageTemplate.vue"
import { ref } from "vue"
import getCookie, { formatRole } from "@/utils/cookiesUtils"
import { Separator } from "@/components/ui/separator"

const message = ref("World")

const role = getCookie("role") || "Aucun rôle"
message.value = formatRole(role)
</script>

<template>
	<PageTemplate>
		<h1 class="text-3xl font-title-bold">Bonjour</h1>
		<Separator />
		<p class="text-md">Votre rôle est : {{ message }}.</p>
	</PageTemplate>
</template>