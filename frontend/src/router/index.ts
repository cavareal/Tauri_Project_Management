import { createRouter, createWebHistory } from "vue-router"
const base = process.env.NODE_ENV === 'production' ? '/tauri/' : '/';

const router = createRouter({
	// eslint-disable-next-line @typescript-eslint/no-unsafe-argument,@typescript-eslint/no-unsafe-member-access
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			path: base + "login",
			name: "login",
			component: () => import("@/components/pages/ConnectionView.vue")
		},
		{
			path: base,
			name: "dashboard",
			component: () => import("@/components/pages/DashboardView.vue")
		},
		{
			path: base + "teams",
			name: "teams",
			component: () => import("@/components/pages/TeamsView.vue")
		},
		{
			path: base + "students",
			name: "students",
			component: () => import("@/components/pages/StudentsView.vue")
		},
		{
			path: base + "sprints",
			name: "sprints",
			component: () => import("@/components/pages/SprintsView.vue")
		},
		{
			path: base + "grades",
			name: "grades",
			component: () => import("@/components/pages/GradeView.vue")
		},
		{
			path: base + "settings",
			name: "settings",
			component: () => import("@/components/pages/SettingsPage.vue")
		},
		{
			path: base + "rating",
			name: "rating",
			component: () => import("@/components/pages/EvaluationView.vue")
		},
		{
			path: base + "test",
			name: "test",
			component: () => import("@/components/pages/TestPage.vue")
		},
		{
			path: base + ":pathMatch(.*)*",
			name: "not-found",
			component: () => import("@/components/pages/NotFoundView.vue")
		},
		{
			path: base + "test2",
			name: "test2",
			component: () => import("@/components/pages/Test2Page.vue")

		}
	]
})

export default router