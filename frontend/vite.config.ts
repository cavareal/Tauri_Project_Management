import path from "path"
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

import tailwind from "tailwindcss"
import autoprefixer from "autoprefixer"

// https://vitejs.dev/config/
export default defineConfig({
	base: '/', // Base path for build files, for deploiement
	css: {
		postcss: {
			plugins: [tailwind(), autoprefixer()],
		},
	},
	plugins: [
		vue(),
	],
	resolve: {
		alias: {
			"@": path.resolve(__dirname, "./src"),
		},
	},
})
