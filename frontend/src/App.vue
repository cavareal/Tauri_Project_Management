<template>
	<div id="app">
		<Example/>
	</div>
</template>

<script>
import Example from "@/components/Example/Example.vue"

import "./index.css"
import "flowbite"

export default {
	name: "App",
	components: {
		Example
	}
}
</script>


<style>
@import './assets/styles/variables.css';

#app {
	position: fixed;
	top: 0;
	left: 0;
	bottom: 0;
	right: 0;
	overflow: hidden;
}
</style>