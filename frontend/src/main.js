import Vue from "vue"
import App from "./App.vue"

Vue.config.productionTip = false

// Variable global, lien serveur
// Vue.prototype.$servApp = 'https://172.24.6.20/app/'

// Variable d'environement, lien localhost
Vue.prototype.$servApp = "http://127.0.0.1:8080/"


new Vue({
	render: h => h(App)
}).$mount("#app")