<script setup>
import {
	ResizableHandle,
	ResizablePanel,
	ResizablePanelGroup
} from "@/components/ui/resizable"
</script>

<template>
	<ResizablePanelGroup direction="horizontal">
		<ResizablePanel class="bg-gray-200 flex items-center justify-center min-w-56 max-w-96">Sidebar</ResizablePanel>
		<ResizableHandle />
		<ResizablePanel class="flex items-center justify-center">Content</ResizablePanel>
	</ResizablePanelGroup>
</template>

<style scoped>

</style>