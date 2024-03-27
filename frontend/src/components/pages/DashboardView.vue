<script setup lang="ts">
import PageTemplate from "@/components/organisms/PageTemplate.vue"
import { ref } from "vue"
import getCookie from "@/utils/cookiesUtils.ts"

const message = ref("World")

message.value = getCookie("role")
</script>

<template>
	<PageTemplate>
		Hello {{ message }}
	</PageTemplate>
</template>