<script setup lang="ts">
import { Bell, GraduationCap, LayoutDashboard, LogOut, Settings, Users } from "lucide-vue-next"
import SidebarLink from "@/components/molecules/SidebarLink.vue"
import Separator from "@/components/ui/separator/Separator.vue"
import { cn } from "@/utils/utils"

const props = defineProps<{
	class?: string
}>()

const style = cn(
	"w-full h-full p-2",
	"flex flex-col items-stretch justify-between gap-2",
	props.class
)
</script>

<template>
	<nav :class="style">
		<div>
			<div class="flex flex-row items-end justify-center gap-4 my-2">
				<img src="/icon.svg" alt="Logo" class="w-8" />
				<h3 class="text-2xl font-title-medium">Tauri</h3>
			</div>
			<SidebarLink link="/">
				<LayoutDashboard /> Tableau de bord
			</SidebarLink>
			<Separator />
			<SidebarLink link="/teams">
				<Users /> Équipes
			</SidebarLink>
			<SidebarLink link="/students">
				<GraduationCap /> Étudiants
			</SidebarLink>
			<SidebarLink link="/grades">
				<GraduationCap /> Notes
			</SidebarLink>
		</div>

		<div>
			<Separator />
			<SidebarLink link="/">
				<Bell /> Notifications
			</SidebarLink>
			<SidebarLink link="/settings">
				<Settings /> Paramètres
			</SidebarLink>
			<SidebarLink link="/login">
				<LogOut /> Déconnexion
			</SidebarLink>
		</div>
	</nav>
</template>