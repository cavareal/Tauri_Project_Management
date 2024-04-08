<script setup lang="ts">
import {
	Dialog,
	DialogContent,
	DialogDescription,
	DialogFooter,
	DialogHeader,
	DialogTitle,
	DialogTrigger,
	DialogClose
} from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { setCurrentPhase } from "@/services/project-service"

const prepublishTeams = async() => {
	await setCurrentPhase("PUBLISHED").then(() => location.reload())
}
</script>

<template>
	<Dialog>
		<DialogTrigger>
			<slot />
		</DialogTrigger>

		<DialogContent>
			<DialogHeader>
				<DialogTitle>Prépublier les équipes</DialogTitle>
				<DialogDescription>Êtes-vous bien sûr de vouloir prépublier la composition des équipes ?</DialogDescription>
			</DialogHeader>

			<DialogFooter class="space-x-2">
				<DialogClose>
					<Button variant="outline">
						Annuler
					</Button>
				</DialogClose>
				<DialogClose>
					<Button type="submit" @click="prepublishTeams">
						Confirmer
					</Button>
				</DialogClose>
			</DialogFooter>
		</DialogContent>
	</Dialog>
</template>