import { createRouter, createWebHistory } from "vue-router"

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			path: "/login",
			name: "login",
			component: () => import("@/components/pages/ConnectionView.vue")
		},
		{
			path: "/",
			name: "dashboard",
			component: () => import("@/components/pages/DashboardView.vue")
		},
		{
			path: "/teams",
			name: "teams",
			component: () => import("@/components/pages/TeamsView.vue")
		},
		{
			path: "/settings",
			name: "settings",
			component: () => import("@/components/pages/SettingsPage.vue")
		}
	]
})

export default router