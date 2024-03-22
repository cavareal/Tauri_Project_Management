<script setup>
import {
	ResizableHandle,
	ResizablePanel,
	ResizablePanelGroup
} from "@/components/ui/resizable"
import { ScrollArea } from "@/components/ui/scroll-area/index.js"
</script>

<template>
	<ResizablePanelGroup direction="horizontal">
		<ResizablePanel class="bg-gray-200 flex items-center justify-center min-w-56 max-w-96">Sidebar</ResizablePanel>
		<ResizableHandle />
		<ResizablePanel class="flex items-center justify-center">
      <ScrollArea class="h-[100%] w-[100%] rounded-md border p-4">
        <ScrollBar/>
      </ScrollArea>
    </ResizablePanel>
	</ResizablePanelGroup>
</template>

<style scoped>

</style>