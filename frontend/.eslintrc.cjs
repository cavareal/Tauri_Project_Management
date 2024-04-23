/* eslint-env node */
// require("@rushstack/eslint-patch/modern-module-resolution")

module.exports = {
	root: true,
	"extends": [
		"@maxencebonamy",
		"plugin:vue/vue3-essential",
		"eslint:recommended",
		"@vue/eslint-config-typescript"
	],
	parserOptions: {
		ecmaVersion: "latest",
		project: "./tsconfig.app.json"
	},
	ignorePatterns: [
		"src/components/ui/**/*.vue",
		"src/components/ui/**/*.ts",
		"tailwind.config.js",
		"postcss.config.js",
		"vite.config.js"
	],
	rules: {
		"vue/multi-word-component-names": "off",
		"vue/no-reserved-component-names": "off",
		"@typescript-eslint/explicit-function-return-type": "off",
		"func-style": ["error", "expression", { "allowArrowFunctions": true }],
	}
}
