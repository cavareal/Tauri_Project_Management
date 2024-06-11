<script setup lang="ts">
import {
	DropdownMenu,
	DropdownMenuContent,
	DropdownMenuGroup,
	DropdownMenuItem,
	DropdownMenuTrigger
} from "@/components/ui/dropdown-menu"
import { Ellipsis, Trash2, PencilLine  } from "lucide-vue-next"
import type { Student } from "@/types/student"
import { useMutation } from "@tanstack/vue-query"
import { updateStudent } from "@/services/student"
import UpdateStudentDialog from "../../organisms/students/EditStudentDialog.vue"
import type { Grade } from "@/types/grade"
import { EditStudentDialog } from "@/components/organisms/students"
import {
	AlertDialog, AlertDialogAction,
	AlertDialogCancel,
	AlertDialogContent,
	AlertDialogDescription, AlertDialogFooter,
	AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger
} from "@/components/ui/alert-dialog"
import { Button } from "@/components/ui/button"

const props = defineProps<{
	student: Student
	mark : number
}>()

// const {mutate : update, error, isPending} = useMutation({mutationKey: ["update-student"], mutationFn: async() => {
// 	await updateStudent(props.student.id, {
// 	}
// 	})
// })

</script>

<template>
	<DropdownMenu>
		<DropdownMenuTrigger as-child>
			<div>
				<Ellipsis class="stroke-gray-800"/>
			</div>
		</DropdownMenuTrigger>
		<DropdownMenuContent>
			<DropdownMenuGroup>
				<DropdownMenuItem>
					<PencilLine class="mr-2 h-4 w-4" />
<!--					<span>Modifier</span>-->
<!--					<EditStudentDialog :student="props.student" :grade="props.mark" @click.stop>-->
<!--						<Button>Modifier</Button>-->
<!--					</EditStudentDialog>-->
					<AlertDialog>
						<AlertDialogTrigger as-child>
							<Button variant="outline">
								Show Dialog
							</Button>
						</AlertDialogTrigger>
						<AlertDialogContent>
							<AlertDialogHeader>
								<AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
								<AlertDialogDescription>
									This action cannot be undone. This will permanently delete your
									account and remove your data from our servers.
								</AlertDialogDescription>
							</AlertDialogHeader>
							<AlertDialogFooter>
								<AlertDialogCancel>Cancel</AlertDialogCancel>
								<AlertDialogAction>Continue</AlertDialogAction>
							</AlertDialogFooter>
						</AlertDialogContent>
					</AlertDialog>
				</DropdownMenuItem>
				<DropdownMenuItem>
					<Trash2 class="mr-2 h-4 w-4" />
					<span>Supprimer</span>
				</DropdownMenuItem>
			</DropdownMenuGroup>
		</DropdownMenuContent>
	</DropdownMenu>
</template>

<style scoped>

</style>