import { createRouter, createWebHistory } from "vue-router"

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			path: "/",
			name: "dashboard",
			component: () => import("../views/DashboardView.vue")
		},
		{
			path: "/about",
			name: "about",
			// route level code-splitting
			// this generates a separate chunk (About.[hash].js) for this route
			// which is lazy-loaded when the route is visited.
			component: () => import("../views/AboutView.vue")
		},
		{
			path: "/connection",
			name: "connection",
			component: () => import("../views/ConnectionView.vue")
		}
	]
})

export default router