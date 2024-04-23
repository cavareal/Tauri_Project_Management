<script setup lang="ts">

import { Row, Column } from "@/components/atoms/containers"
import InfoText from "@/components/atoms/texts/InfoText.vue"
import Subtitle from "@/components/atoms/texts/Subtitle.vue"

defineProps<{
	title: string
	description: string
}>()

</script>

<template>
	<Row class="items-center border rounded-lg p-6">
			<Column class="items-start justify-center flex-1 gap-1">
				<Subtitle>{{ title }}</Subtitle>
				<InfoText>{{ description }}</InfoText>
			</Column>

			<slot />
		</Row>
</template>