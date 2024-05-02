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
import { Row } from "@/components/atoms/containers"

const { data: notifications }
    = useQuery({ queryKey: ["notifications"], queryFn: getAllNotifications })

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
      <Row v-if="!notifications" class="mt-5">Vous n'avez aucune notification pour le moment</Row>
      <NotificationTable v-else :notifications="notifications ?? null"/>
     </SheetContent>
   </Sheet>

 </template>

 <style scoped>

 </style>