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
import { deleteAllTeams } from "@/services/team-service"

const deleteTeams = async() => {
	await deleteAllTeams().then(() => location.reload())
}
</script>

<template>
	<Dialog>
		<DialogTrigger>
			<slot />
		</DialogTrigger>

		<DialogContent>
			<DialogHeader>
				<DialogTitle>Supprimer les équipes</DialogTitle>
				<DialogDescription>Êtes-vous bien sûr de vouloir supprimer toutes les équipes ?</DialogDescription>
			</DialogHeader>

			<DialogFooter class="space-x-2">
				<DialogClose>
					<Button variant="outline">
						Annuler
					</Button>
				</DialogClose>
				<DialogClose>
					<Button type="submit" @click="deleteTeams">
						Confirmer
					</Button>
				</DialogClose>
			</DialogFooter>
		</DialogContent>
	</Dialog>
</template>