<script setup>
import PageTemplate from "@/components/organisms/PageTemplate.vue"
import { ref } from "vue"
import  getCookie  from "@/utils/cookiesUtils.js"

const message = ref("World")

message.value = getCookie("role")
</script>

<template>
	<PageTemplate>
    Hello {{message}}
	</PageTemplate>
</template>

<style scoped>

</style>