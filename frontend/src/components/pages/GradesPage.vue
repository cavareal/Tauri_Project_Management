<script setup lang="ts">
import { SidebarTemplate } from "@/components/templates"
import TMOwnGradeView from "@/components/organisms/Grade/TMOwnGradeView.vue"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Header } from "@/components/molecules/header"
import { hasPermission } from "@/services/user-service"
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { onMounted, ref } from "vue"
import { useQuery } from "@tanstack/vue-query"
import { getAllSprints } from "@/services/sprint-service"
import { Separator } from "@/components/ui/separator"
import FeedbackContainer from "@/components/organisms/rating/FeedbackContainer.vue"
import type { Team } from "@/types/team"
import { getTeamByUserId } from "@/services/team-service"
import { Cookies } from "@/utils/cookie"

const canSeeNotes = hasPermission("GRADES_PAGE")
const selectedSprint = ref("")
const componentKey = ref(0)
const team = ref<Team>()


const { data: sprints } = useQuery({ queryKey: ["sprints"], queryFn: getAllSprints })

const forceRerender = () => {
	componentKey.value += 1
}

onMounted(async() => {
	team.value = await getTeamByUserId(Cookies.getUserId())
})

</script>

<template>
	<SidebarTemplate>
    <Header title="Notes" />
				<Tabs default-value="Mes notes" class="flex flex-col flex-grow">
          <TabsList>
					<TabsTrigger value="Mes notes" class="flex-grow">
						Mes notes
					</TabsTrigger>
					<TabsTrigger value="Mon équipe" class="flex-grow">
            Mon équipe
					</TabsTrigger>
					<TabsTrigger value="Equipes" class="flex-grow">
            Equipes
					</TabsTrigger>
          </TabsList>
          <TabsContent value="Mes notes">
            <TMOwnGradeView />
          </TabsContent>
          <TabsContent value="Mon équipe" >
            <div class="flex flex-row">
            <h2>Feedbacks</h2>
            <Select v-model="selectedSprint">
              <SelectTrigger class="w-[180px]">
                <SelectValue placeholder="Sprint par défaut" />
              </SelectTrigger>
              <SelectContent>
                <SelectGroup>
                  <SelectItem v-for="sprint in sprints" :key="sprint.id" :value="sprint.id" @click="forceRerender">{{sprint.id}}</SelectItem>
                </SelectGroup>
              </SelectContent>
            </Select>
            </div>
            <Separator/>
            <FeedbackContainer v-if="team && selectedSprint !== ''"
                               title="Feedbacks"
                               infoText="Voit les feedbacks qu'on laisser vos encadrant sur le déroulement du sprint"
                               :sprintId="selectedSprint"
                               :teamId="team.id"/>
          </TabsContent>
          <TabsContent value="Equipes">
          </TabsContent>
				</Tabs>
	</SidebarTemplate>
</template>