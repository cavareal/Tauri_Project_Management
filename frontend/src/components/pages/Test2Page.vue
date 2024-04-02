<script setup lang="ts">
import PageTemplate from "@/components/organisms/PageTemplate.vue"
import { defineComponent } from "vue"
import Tab from "@/components/ui/tab/Tab.vue"
import Tabs from "@/components/ui/tab/Tabs.vue"

defineComponent({
	// eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
	components: { PageTemplate, Tabs, Tab },
	data: () => {
		return { dynamicTabs: [1, 2, 3] }
	}
})
</script>

<template>
  <PageTemplate>
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
  </PageTemplate>
</template>