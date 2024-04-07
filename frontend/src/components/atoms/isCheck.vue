<script setup lang="ts">
const props = defineProps({
	isCheck: {
		type: Boolean,
		required: true
	}
})

</script>

<template>
  <div v-if="isCheck">
    <svg xmlns="http://www.w3.org/2000/svg"
         width="15"
         height="15"
         viewBox="0 0 20 20"
         fill="none"
         stroke="green"
         stroke-width="2"
         stroke-linecap="round"
         stroke-linejoin="round"
         class="lucide lucide-check">
      <path d="M20 6 9 17l-5-5"/>
    </svg>
  </div>
  <div v-else>
    <svg xmlns="http://www.w3.org/2000/svg"
         width="15"
         height="15"
         viewBox="0 0 20 20"
         fill="none"
         stroke="red"
         stroke-width="2"
         stroke-linecap="round"
         stroke-linejoin="round"
         class="lucide lucide-x">
      <path d="M18 6 6 18"/>
      <path d="m6 6 12 12"/>
    </svg>
  </div>
</template>

<style scoped>

</style>