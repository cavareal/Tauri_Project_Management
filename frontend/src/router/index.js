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
			path: "/students",
			name: "students",
			component: () => import("@/components/pages/StudentsView.vue")
		},
		{
			path: "/settings",
			name: "settings",
			component: () => import("@/components/pages/SettingsPage.vue")
		},
		{
			path: "/test",
			name: "test",
			component: () => import("@/components/pages/TestPage.vue")
		}
	]
})

export default router