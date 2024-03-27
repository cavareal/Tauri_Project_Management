<script>
import PageTemplate from "@/components/organisms/PageTemplate.vue"
import StudentsTable from "@/components/organisms/StudentsTable.vue"

export default {
	name: "StudentsView",
	components: { StudentsTable, PageTemplate }
}
</script>

<template>
	<PageTemplate class="">
		<h1 class="text-3xl font-title-bold">Étudiants</h1>
		<StudentsTable/>
	</PageTemplate>
</template>

<style scoped>

</style>