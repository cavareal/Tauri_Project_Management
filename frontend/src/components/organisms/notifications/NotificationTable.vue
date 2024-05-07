<script setup lang="ts">

import type { Notification } from "@/types/notification"
import NotificationElement from "@/components/molecules/notifications/NotificationElement.vue"
import { changeStateChecked } from "@/services/notification-service"

const emits = defineEmits(["open:notifications", "read:notifications"])

defineProps<{
  notifications: Notification[] | null
}>()

const markNotificationAsRead = async(notificationId: number) => {
	await changeStateChecked(notificationId)
		.then(() => emits("read:notifications"))
}
</script>

<template>
  <div v-for="(notification, i) in notifications" :key="i">
    <NotificationElement v-if="!notification.checked" :title="notification.userFrom.name"
:description="notification.message" @read:notifications="markNotificationAsRead(notification.id)"/>
  </div>
</template>