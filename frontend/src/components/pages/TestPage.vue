<script setup lang="ts">
import { ref, onMounted } from "vue"
import { columns } from "@/components/test/columns"
import DataTable from "@/components/test/DataTable.vue"

const data = ref([])

function getData() {
	// Fetch data from your API here.
	return [
		{
			id: "728ed52f",
			amount: 100,
			status: "pending",
			email: "m@example.com"
		}
		// ...
	]
}

onMounted(() => {
	data.value = getData()
})
</script>

<template>
	<div class="container py-10 mx-auto">
		<DataTable :columns="columns" :data="data" />
	</div>
</template>