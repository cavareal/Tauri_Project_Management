<script setup lang="ts">
import PageTemplate from "@/components/organisms/template/PageTemplate.vue"
import { ref } from "vue"
import getCookie from "@/utils/cookiesUtils"

const message = ref("World")

message.value = getCookie("role") ?? "World"
</script>

<template>
	<PageTemplate>
		Hello {{ message }}
	</PageTemplate>
</template>