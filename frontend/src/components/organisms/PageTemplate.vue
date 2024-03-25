<script>
import { ResizableHandle, ResizablePanel, ResizablePanelGroup } from "@/components/ui/resizable/index.js"
import Sidebar from "@/components/organisms/Sidebar.vue"
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area/index.js"

export default {
	name: "PageTemplate",
	components: { ResizablePanelGroup, ResizableHandle, ScrollArea, ScrollBar, Sidebar, ResizablePanel }
}
</script>

<template>
	<ResizablePanelGroup direction="horizontal">
		<ResizablePanel class="" :default-size="16" :min-size="16" :max-size="24">
			<Sidebar />
		</ResizablePanel>
		<ResizableHandle />
		<ResizablePanel class="flex items-center justify-center">
			<ScrollArea class="h-[100%] w-[100%] rounded-md border p-4">
				<slot/>
				<ScrollBar/>
			</ScrollArea>
		</ResizablePanel>
	</ResizablePanelGroup>
</template>

<style scoped>

</style>