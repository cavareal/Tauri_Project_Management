<script setup lang="ts">

import type { Notification } from "@/types/notification"
import NotificationElement from "@/components/molecules/notifications/NotificationElement.vue"

const emits = defineEmits(["open:notifications"])

defineProps<{
  notifications: Notification[] | null
}>()
</script>

<template>
  <div v-for="(notification, i) in notifications" :key="i">
    <NotificationElement :title="notification.userFrom.name" :description="notification.message" />
  </div>
</template>

<style scoped>

</style>