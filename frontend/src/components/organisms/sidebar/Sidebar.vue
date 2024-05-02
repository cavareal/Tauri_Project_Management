<script setup lang="ts">

import { Bell, GraduationCap, LayoutDashboard, LogOut, Scale, Users, Check, Tag, Play, User } from "lucide-vue-next"
import SidebarLink from "./SidebarLink.vue"
import Separator from "@/components/ui/separator/Separator.vue"
import { Logo } from "@/components/atoms/logo"
import { cn } from "@/utils/style"
import { Column } from "@/components/atoms/containers"
import Title from "@/components/atoms/texts/Title.vue"
import Row from "@/components/atoms/containers/Row.vue"
import NotificationView from "@/components/organisms/notifications/NotificationView.vue"

const props = defineProps<{
	class?: string
}>()

const style = cn(
	"w-64 h-full p-6",
	"flex flex-col items-stretch justify-between gap-2",
	"bg-dark-blue drop-shadow-sidebar",
	props.class
)

</script>

<template>
	<nav :class="style">
		<Column>
			<Row class="items-center justify-center gap-4 mt-4 mb-8">
				<Logo class="h-12 fill-light-blue" />
				<Title class="text-3xl uppercase font-title-medium text-light-blue">Tauri</Title>
			</Row>

			<SidebarLink link="/">
				<LayoutDashboard /> Tableau de bord
			</SidebarLink>

			<Separator class="my-2" />

			<SidebarLink link="/students" permission="STUDENTS_PAGE">
				<GraduationCap /> Étudiants
			</SidebarLink>

			<SidebarLink link="/teams" permission="TEAMS_PAGE">
				<Users /> Équipes
			</SidebarLink>

			<SidebarLink link="/my-team" permission="MY_TEAM_PAGE">
				<User /> Mon équipe
			</SidebarLink>

			<SidebarLink link="/sprints" permission="SPRINTS_PAGE">
				<Play /> Sprints
			</SidebarLink>

			<SidebarLink link="/grades" permission="GRADES_PAGE">
				<Tag /> Notes
			</SidebarLink>

			<SidebarLink link="/rating" permission="RATING_PAGE">
				<Check /> Évaluations
			</SidebarLink>

			<SidebarLink link="/grade-scales" permission="GRADE_SCALES_PAGE">
				<Scale /> Barèmes
			</SidebarLink>
		</Column>

		<Column>
			<Separator class="my-2" />

      <NotificationView>
        <SidebarLink>
          <Bell /> Notifications
          <div class="absolute top-0 right-0  bg-red-600 rounded-full"></div>
        </SidebarLink>
      </NotificationView>

			<SidebarLink link="/login">
				<LogOut /> Déconnexion
			</SidebarLink>
		</Column>
	</nav>
</template>