import "./assets/main.css"

import { createApp } from "vue"
import App from "./App.vue"
import router from "./router"
import { VueQueryPlugin, type VueQueryPluginOptions } from "@tanstack/vue-query"

const app = createApp(App)

app.use(router)

const vueQueryPluginOptions: VueQueryPluginOptions = {
	queryClientConfig: {
		defaultOptions: {
			queries: {
				refetchOnWindowFocus: false
			}
		}
	}
}
app.use(VueQueryPlugin, vueQueryPluginOptions)

app.mount("#app")