<script setup lang="ts">
import PageTemplate from "@/components/organisms/PageTemplate.vue"
import { ref } from "vue"
import getCookie from "@/utils/cookiesUtils"

const message = ref("World")

message.value = getCookie("role") ?? "World"
</script>

<template>
	<PageTemplate>
		Hello {{ message }}
		<TeamTable />
		<TeamTable />
	</PageTemplate>
</template>