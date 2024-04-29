<script setup lang="ts">

import {
	Sheet, SheetContent,
	SheetHeader,
	SheetTitle,
	SheetTrigger
} from "@/components/ui/sheet"
import { Header } from "@/components/molecules/header"
import NotificationElement from "@/components/molecules/notifications/NotificationElement.vue"

const ACTION_TITLE = "R. Woodward"
const ACTION_DESCRIPTION = "La composition des équipes a été prépubliée."

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
      <NotificationElement :title="ACTION_TITLE" :description="ACTION_DESCRIPTION" />
    </SheetContent>
  </Sheet>

</template>

<style scoped>

</style>