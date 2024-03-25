<script>
import Button from "@/components/ui/button/Button.vue"
import { cn } from "@/utils/utils.js"

export default {
	name: "SidebarLink",
	components: { Button },
	props: {
		selected: {
			type: Boolean,
			default: false
		},
		link: {
			type: String,
			default: ""
		},
		className: {
			type: String,
			default: ""
		}
	},
	computed: {
		style() {
			return cn(
				"w-full my-1",
				"flex flex-row items-center justify-start gap-2",
				this.className
			)
		}
	}
}
</script>

<template>
	<RouterLink :to="link">
		<Button :variant="selected ? 'secondary' : 'ghost'" :class="style">
			<slot/>
		</Button>
	</RouterLink>
</template>

<style scoped>

</style>