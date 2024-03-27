<script>
import { ResizableHandle, ResizablePanel, ResizablePanelGroup } from "@/components/ui/resizable/index.js"
import Sidebar from "@/components/organisms/Sidebar.vue"
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area/index.js"
import { cn } from "@/utils/utils.js"

export default {
	name: "PageTemplate",
	components: { ResizablePanelGroup, ResizableHandle, ScrollArea, ScrollBar, Sidebar, ResizablePanel },
	props: {
		className: {
			type: String,
			default: ""
		}
	},
	computed: {
		style() {
			return cn(
				"h-full w-full p-8",
				this.className
			)
		}
	}
}
</script>

<template>
	<ResizablePanelGroup direction="horizontal">
		<ResizablePanel class="" :default-size="16" :min-size="16" :max-size="24">
			<Sidebar />
		</ResizablePanel>
		<ResizableHandle />
		<ResizablePanel class="flex items-center justify-center">
			<ScrollArea :class="style">
				<slot/>
				<ScrollBar/>
			</ScrollArea>
		</ResizablePanel>
	</ResizablePanelGroup>
</template>

<style scoped>

</style>