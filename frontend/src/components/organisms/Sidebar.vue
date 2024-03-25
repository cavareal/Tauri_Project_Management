<script>
import { Bell, GraduationCap, LayoutDashboard, LogOut, Settings, Users } from "lucide-vue-next"
import SidebarLink from "@/components/molecules/SidebarLink.vue"
import Separator from "@/components/ui/separator/Separator.vue"
import { cn } from "@/utils/utils.js"

export default {
	name: "Sidebar",
	methods: { Users },
	components: { Separator, SidebarLink, LayoutDashboard, Users, Bell, Settings, LogOut, GraduationCap },
	props: {
		className: {
			type: String,
			default: ""
		}
	},
	computed: {
		style() {
			return cn(
				"w-full h-full p-2",
				"flex flex-col items-stretch justify-between gap-2",
				// "bg-dark-blue",
				this.className
			)
		}
	}
}
</script>

<template>
	<nav :class="style">
		<div>
			<SidebarLink link="/">
				<LayoutDashboard/> Tableau de bord
			</SidebarLink>
			<Separator/>
			<SidebarLink link="/teams">
				<Users/> Équipes
			</SidebarLink>
			<SidebarLink link="/students">
				<GraduationCap/> Étudiants
			</SidebarLink>
		</div>

		<div>
			<Separator/>
			<SidebarLink link="/">
				<Bell/> Notifications
			</SidebarLink>
			<SidebarLink link="/settings">
				<Settings/> Paramètres
			</SidebarLink>
			<SidebarLink link="/">
				<LogOut/> Déconnexion
			</SidebarLink>
		</div>
	</nav>
</template>

<style scoped>

</style>