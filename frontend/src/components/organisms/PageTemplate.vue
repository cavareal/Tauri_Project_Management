<script setup lang="ts">
import { computed } from "vue"
import { ResizableHandle, ResizablePanel, ResizablePanelGroup } from "@/components/ui/resizable/index"
import Sidebar from "@/components/organisms/Sidebar.vue"
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area/index"
import { cn } from "@/utils/utils"

const props = defineProps<{
	className?: string
}>()

const style = computed(() => {
	return cn(
		"h-full w-full p-8",
		props.className
	)
})
</script>

<template>
	<ResizablePanelGroup direction="horizontal">
		<ResizablePanel class="" :default-size="16" :min-size="16" :max-size="24">
			<Sidebar />
		</ResizablePanel>
		<ResizableHandle />
		<ResizablePanel class="flex items-center justify-center">
			<ScrollArea :class="style">
				<slot />
				<ScrollBar />
			</ScrollArea>
		</ResizablePanel>
	</ResizablePanelGroup>
</template>