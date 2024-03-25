<script setup>
import {
	ResizableHandle,
	ResizablePanel,
	ResizablePanelGroup
} from "@/components/ui/resizable"
import Sidebar from "@/components/organisms/Sidebar.vue"
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area"
</script>

<template>
	<ResizablePanelGroup direction="horizontal">
		<ResizablePanel class="" :default-size="16" :min-size="16" :max-size="24">
			<Sidebar />
		</ResizablePanel>
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