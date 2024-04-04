<script setup lang="ts">

const props = defineProps<{
	class?: string
}>()

</script>

<template>
	<div class="flex flex-col" :class="props.class">
		<slot />
	</div>
</template>