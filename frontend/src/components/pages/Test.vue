<script setup lang="ts">
import { ref } from "vue"

const meals = ref([
	"Hamburger",
	"Pizza",
	"Spaghetti",
	"Tacos",
	"Teriyaki Chicken"
])

const handleDrop = (event: DragEvent) => {
	event.preventDefault()
	const data = event.dataTransfer?.getData("text/plain")
	console.log("drop", data)
}

const handleDragStart = (event: DragEvent, itemData: string) => {
	event.dataTransfer?.setData("text/plain", JSON.stringify({ data: itemData }))
	console.log("drag", itemData)
}


</script>

<template>
	<h1>Favorite Foods</h1>
	<ul
		v-on:dragover="(e: DragEvent) => e.preventDefault()"
		v-on:drop="(e: DragEvent) => handleDrop(e)"
	>
		<li
			v-for="(meal, i) in meals" :key="i"
			draggable="true" v-on:dragstart="(e: DragEvent) => handleDragStart(e, meal)"
		>
			{{ meal }}
		</li>
	</ul>
</template>