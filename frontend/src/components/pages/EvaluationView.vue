<script setup lang="ts">
import { ref } from "vue"
import PageTemplate from "@/components/organisms/template/PageTemplate.vue"
import { defineComponent } from "vue"
import Tab from "@/components/ui/tab/Tab.vue"
import Tabs from "@/components/ui/tab/Tabs.vue"
import NotAutorized from "@/components/organisms/Teams/NotAuthorized.vue"
import TMRateView from "@/components/organisms/Rate/TMRateView.vue"
import getCookie from "@/utils/cookiesUtils"

const token = getCookie("token")
const role = getCookie("role")
const sprintList = ref([1, 2, 3])

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
    <h1 class="text-3xl font-title-bold">Evaluation</h1>
    <div class="tabs-example">
      <div class="example example-1">
        <Tabs>
          <template v-for="(sprint, index) in sprintList" :key="index">
            <Tab :title="`Sprint ${index + 1}`">
              <NotAutorized v-if="!token || !role" />
              <TMRateView v-else-if="role === 'TM'"/>
              <NotAutorized v-else />
            </Tab>
          </template>
        </Tabs>
      </div>
    </div>
  </PageTemplate>
</template>