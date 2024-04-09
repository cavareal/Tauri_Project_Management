<script setup lang="ts">
import getCookie from "@/utils/cookiesUtils"
import { onMounted, ref, watch } from "vue"
import type { ProjectPhase } from "@/types/project"
import type { Team } from "@/types/team"
import PageTemplate from "../organisms/template/PageTemplate.vue"
import { Row } from "@/components/atoms/containers"
import { Separator } from "@/components/ui/separator"
import MyTeamCreated from "@/components/organisms/my-team/MyTeamCreated.vue"
import { NotAuthorized, NotFound } from "@/components/organisms/errors"
import { getTeamBySSId } from "@/services/team-service"
import { getCurrentPhase } from "@/services/project-service"

const token = getCookie("token")
const role = getCookie("role")
const currentUser = getCookie("current_user")
const currentPhase = ref<ProjectPhase>("COMPOSING")
const team = ref<Team>()

watch(() => { }, async() => {
	currentPhase.value = await getCurrentPhase()
}, { immediate: true })

onMounted(async() => {
	const data = await getTeamBySSId(currentUser)
	team.value = data
})
</script>

<template>
  <PageTemplate>
    <Row class="items-center justify-between">
      <h1 class="text-3xl font-title-bold">Équipes</h1>
    </Row>
    <Separator/>
    <NotAuthorized v-if="!token || !role" />
    <MyTeamCreated v-else-if="role ==='SUPERVISING_STAFF' && currentPhase !== 'COMPOSING' && team" :team="team" :phase="currentPhase"/>
    <NotFound v-else/>
  </PageTemplate>
</template>

<style scoped>

</style>