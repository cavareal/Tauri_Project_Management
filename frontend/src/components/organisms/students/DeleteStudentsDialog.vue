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
import { deleteAllStudents } from "@/services/student-service"

const deleteStudents = async() => {
	await deleteAllStudents().then(() => location.reload())
}
</script>

<template>
	<Dialog>
		<DialogTrigger>
			<slot />
		</DialogTrigger>

		<DialogContent>
			<DialogHeader>
				<DialogTitle>Supprimer les étudiants</DialogTitle>
				<DialogDescription>Êtes-vous bien sûr de vouloir supprimer tous les étudiants de la base de données ?</DialogDescription>
			</DialogHeader>

			<DialogFooter class="space-x-2">
				<DialogClose>
					<Button variant="outline">
						Annuler
					</Button>
				</DialogClose>
				<DialogClose>
					<Button type="submit" @click="deleteStudents">
						Confirmer
					</Button>
				</DialogClose>
			</DialogFooter>
		</DialogContent>
	</Dialog>
</template>