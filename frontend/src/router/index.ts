import { createRouter, createWebHistory } from "vue-router"

const router = createRouter({
	// eslint-disable-next-line @typescript-eslint/no-unsafe-argument,@typescript-eslint/no-unsafe-member-access
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
			path: "/grades",
			name: "grades",
			component: () => import("@/components/pages/GradeView.vue")
		},
		{
			path: "/settings",
			name: "settings",
			component: () => import("@/components/pages/SettingsPage.vue")
		},
		{
			path:"/rating"
			,name:"rating"
			,component: () => import("@/components/pages/EvaluationView.vue")
		},
		{
			path: "/test",
			name: "test",
			component: () => import("@/components/pages/TestPage.vue")
		},
		{
			path: "/:pathMatch(.*)*",
			name: "not-found",
			component: () => import("@/components/pages/NotFoundView.vue")
		},
		{
			path: "/test2",
			name: "test2",
			component: () => import("@/components/pages/Test2Page.vue")

		}
	]
})

export default router