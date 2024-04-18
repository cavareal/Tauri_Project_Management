<script setup lang="ts">
import { SidebarTemplate } from "@/components/templates"
import { defineComponent } from "vue"
import Tab from "@/components/molecules/tab/Tab.vue"
import Tabs from "@/components/molecules/tab/Tabs.vue"

defineComponent({
	// eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
	data: () => {
		return { dynamicTabs: [1, 2, 3] }
	}
})
</script>

<template>
	<SidebarTemplate>
		<h1 class="font-title-bold">Test 2 Page</h1>
		<div class="tabs-example">
			<div class="example example-1">
				<Tabs>
					<Tab title="Mon équipe">
						<h3>This is Tab 1</h3>
					</Tab>
					<Tab title="équipe">
						<h3>This is Tab 2</h3>
					</Tab>
				</Tabs>
			</div>
		</div>
	</SidebarTemplate>
</template>