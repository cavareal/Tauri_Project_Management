import { createRouter, createWebHistory } from "vue-router"

const router = createRouter({
	// eslint-disable-next-line @typescript-eslint/no-unsafe-argument,@typescript-eslint/no-unsafe-member-access
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			path: "/login",
			name: "login",
			component: () => import("@/components/pages/ConnectionPage.vue")
		},
		{
			path: "/",
			name: "dashboard",
			component: () => import("@/components/pages/DashboardPage.vue")
		},
		{
			path: "/teams",
			name: "teams",
			component: () => import("@/components/pages/TeamsPage.vue")
		},
		{
			path: "/my-team",
			name: "my-team",
			component: () => import("@/components/pages/MyTeamPage.vue")
		},
		{
			path: "/students",
			name: "students",
			component: () => import("@/components/pages/StudentsPage.vue")
		},
		{
			path: "/sprints",
			name: "sprints",
			component: () => import("@/components/pages/SprintsPage.vue")
		},
		{
			path: "/grades",
			name: "grades",
			component: () => import("@/components/pages/GradePage.vue")
		},
		{
			path: "/rating",
			name: "rating",
			component: () => import("@/components/pages/EvaluationPage.vue")
		},
		{
			path: "/test",
			name: "test",
			component: () => import("@/components/pages/TestPage.vue")
		},
		{
			path: "/:pathMatch(.*)*",
			name: "not-found",
			component: () => import("@/components/pages/NotFoundPage.vue")
		},
		{
			path: "/test2",
			name: "test2",
			component: () => import("@/components/pages/Test2Page.vue")
		}
	]
})

export default router