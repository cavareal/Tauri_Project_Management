<script setup lang="ts">
import { Button, type ButtonVariants } from "@/components/ui/button"
import { Loader2 } from "lucide-vue-next"

defineProps<{
	variant?: ButtonVariants["variant"]
	size?: ButtonVariants["size"]
	loading?: boolean
}>()

</script>

<template>
	<Button :variant="variant ?? 'default'" :size="size ?? 'default'" :disabled="loading ?? false">
		<Loader2 class="w-4 h-4 mr-2 animate-spin" v-if="loading ?? false" />
		<slot />
	</Button>
</template>