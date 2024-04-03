<script setup lang="ts">

const props = defineProps<{
	class?: string
}>()

</script>

<template>
	<div class="flex flex-row" :class="props.class">
		<slot />
	</div>
</template>