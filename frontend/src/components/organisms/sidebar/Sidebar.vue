<script setup lang="ts">

import { Bell, GraduationCap, LayoutDashboard, LogOut, Scale, Users, Check, Tag, Play, User } from "lucide-vue-next"
import SidebarLink from "./SidebarLink.vue"
import Separator from "@/components/ui/separator/Separator.vue"
import { Logo } from "@/components/atoms/logo"
import { cn } from "@/utils/style"
import { Button } from "@/components/ui/button"
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
		<div>
			<div class="flex flex-row items-center justify-center gap-4 mt-4 mb-8">
				<Logo class="h-12 fill-light-blue" />
				<h3 class="text-3xl uppercase font-title-medium text-light-blue">Tauri</h3>
			</div>

			<SidebarLink link="/">
				<LayoutDashboard /> Tableau de bord
			</SidebarLink>

			<Separator class="my-2" />

			<SidebarLink link="/students">
				<GraduationCap /> Étudiants
			</SidebarLink>

			<SidebarLink link="/teams">
				<Users /> Équipes
			</SidebarLink>

			<SidebarLink link="/my-team">
				<User /> Mon équipe
			</SidebarLink>

			<SidebarLink link="/sprints">
				<Play /> Sprints
			</SidebarLink>

			<SidebarLink link="/grades">
				<Tag /> Notes
			</SidebarLink>

			<SidebarLink link="/rating">
				<Check /> Évaluations
			</SidebarLink>

			<SidebarLink link="/grade-scales">
				<Scale /> Barèmes
			</SidebarLink>
		</div>

		<div>
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
		</div>
	</nav>
</template>