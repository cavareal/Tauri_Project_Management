<script setup lang="ts">

import InfoText from "../../atoms/texts/InfoText.vue"
import { Column, Row } from "@/components/atoms/containers"
import Subtitle from "../../atoms/texts/Subtitle.vue"
import { Check, ExternalLink } from "lucide-vue-next"
import { Button } from "@/components/ui/button"

const emits = defineEmits(["read:notifications"])

defineProps<{
  title: string
  description: string
}>()

</script>

<template>
  <Column class="mt-5 border rounded-lg p-6">
    <Row >
      <Column class="items-start justify-center flex-1 gap-1">
        <Subtitle>{{ title }}</Subtitle>
        <InfoText>{{ description }}</InfoText>
      </Column>
    </Row>
    <Row class ="mt-2.5 justify-end">
      <Button variant="outline">
        <ExternalLink class="mr-2"/>Voir
      </Button>
      <Button class="mx-4" variant="outline">
        <Check/>
      </Button>
      <div class="absolute top-0 right-0 bg-red-600 rounded-full"></div>
    </Row>
  </Column>
</template>

<style scoped>

</style>