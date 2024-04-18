<script setup lang="ts">

import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"

defineProps<{
	title: string
	description: string
}>()

</script>

<template>
	<Dialog>
		<DialogTrigger>
			<slot name="trigger" />
		</DialogTrigger>

		<DialogContent>
			<DialogHeader>
				<DialogTitle>{{ title }}</DialogTitle>
				<DialogDescription>{{ description }}</DialogDescription>
			</DialogHeader>

			<slot />

			<DialogFooter class="space-x-2">
				<slot name="footer" />
			</DialogFooter>
		</DialogContent>
	</Dialog>
</template>