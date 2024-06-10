<script setup lang="ts">

import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "@/components/ui/tooltip"
import { VueLatex } from "vatex"

defineProps<{
	expression: string
}>()

</script>

<template>
	<TooltipProvider :delay-duration="200" >
		<Tooltip>
			<TooltipTrigger>
				<slot />
			</TooltipTrigger>
			<TooltipContent>
				<p class="max-w-96">
					<VueLatex expression="x = {-b \\pm \\sqrt{b^2-4ac} \\over 2a}." />
				</p>
			</TooltipContent>
		</Tooltip>
	</TooltipProvider>
</template>