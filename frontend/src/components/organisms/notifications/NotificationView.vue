<script setup lang="ts">

import {
	Sheet, SheetContent,
	SheetHeader,
	SheetTitle,
	SheetTrigger
} from "@/components/ui/sheet"
import { Header } from "@/components/molecules/header"
import { useQuery } from "@tanstack/vue-query"
import { getAllNotifications } from "@/services/notification-service"
import NotificationTable from "@/components/organisms/notifications/NotificationTable.vue"
import { Text } from "@/components/atoms/texts"

const { data: notifications, refetch: refetchNotifications }
    = useQuery({ queryKey: ["notifications"], queryFn: getAllNotifications })

const refetch = async() => {
	await refetchNotifications()
}

console.log(getAllNotifications())

</script>

<template>

  <Sheet key = "left">
    <SheetTrigger asChild>
      <slot />
    </SheetTrigger>
    <SheetContent side = "left">
      <SheetHeader>
        <SheetTitle>
          <Header title="Notifications">
          </Header>
        </SheetTitle>
      </SheetHeader>
      <div class="mt-5 h-5/6 overflow-y-auto">
        <Text v-if="!notifications">Vous n'avez aucune notification pour le moment</Text>
        <NotificationTable v-else :notifications="notifications ?? null" @read:notifications="refetch"/>
      </div>
     </SheetContent>
   </Sheet>

 </template>