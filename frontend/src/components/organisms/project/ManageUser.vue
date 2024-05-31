<script setup lang="ts">
import { ref, watch, onMounted, computed } from "vue"
import { Column } from "@/components/atoms/containers"
import { Button } from "@/components/ui/button"
import { useMutation, useQuery } from "@tanstack/vue-query"
import { createToast } from "@/utils/toast"
import { type User } from "@/types/user"
import { Trash } from "lucide-vue-next"
import { deleteUser, getAllUsers } from "@/services/user/user.service"
import { getAllRoles } from "@/services/role/role.service"
import { type Role } from "@/types/role"
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog"

const users = ref<User[]>([])
const roles = ref<Role[]>([])
const userToDelete = ref<number | null>(null)


const { isPending: usersLoading, error: usersError, ...allUsersQuery } = useQuery({ queryKey: ["get-all-users"], queryFn: async () => {
        return getAllUsers()
            .then((data) => users.value = data)
    }
})

const { isPending: rolesLoading, error: rolesError } = useQuery({ queryKey: ["get-all-roles"], queryFn: async () => {
        return getAllRoles()
            .then((data) => roles.value = data)
    }
})

const combinedData = computed(() => {
    return users.value.map(user => ({
        ...user,
        role: getListOfRoles(user)
    }))
})

function getListOfRoles(user: User){
    const rolesOfUser: string[] = []

    roles.value.forEach(role => {
        if(role.user.id === user.id){
            rolesOfUser.push(role.type)
        }
    })
    return rolesOfUser
}


function openDelete(userId: number) {
    userToDelete.value = userId
}

const { mutate: deleteUserMutate } = useMutation({ mutationKey: ["delete-user"], mutationFn: async () => {
        if (userToDelete.value !== null) {
            await deleteUser(userToDelete.value)
                .then((data) => {
                    createToast("L'utilisateur a été supprimé.")
                    allUsersQuery.refetch()
                })
                .catch((error) => createToast("Erreur lors de la suppression de l'utilisateur."))
        }
    }
})


const refetch = async() => {
	await allUsersQuery.refetch()
}

</script>

<template>
    <div class="mt-10 border border-gray-300 border-dashed rounded-lg flex justify-center flex-col items-stretch p-4">
        <h2 class="text-xl font-semibold text-center mb-4">Gestion des utilisateurs</h2>
        <Column class="items-center gap-4">
            <template v-if="usersLoading || rolesLoading">
                <div>Chargement...</div>
            </template>
            <template v-else-if="usersError || rolesError">
                <div>Erreur lors du chargement des données.</div>
            </template>
            <template v-else>
                <div v-for="user in combinedData" :key="user.id"
                    class="flex justify-between items-center w-full p-2 border-b border-gray-300">
                    <div>
                        <p class="font-medium">{{ user.name }}</p>
                        <p class="text-gray-500">{{ user.email }}</p>
                        <p class="text-gray-400">{{ user.role }}</p>
                    </div>
                    <Dialog>
                        <DialogTrigger as-child>
                            <Button class="" @click="openDelete(user.id)">
                                <Trash class="w-5 h-5" />
                            </Button>
                        </DialogTrigger>
                        <DialogContent class="sm:max-w-[425px]">
                            <DialogHeader>
                                <DialogTitle>Confirmer la suppression</DialogTitle>
                                <DialogDescription>
                                    Êtes-vous sûr de vouloir supprimer cet utilisateur ?
                                </DialogDescription>
                            </DialogHeader>
                            <DialogFooter>
                                <Button @click="userToDelete = null">Annuler</Button>
                                <Button class="bg-red-500 text-white" @click="deleteUserMutate">Supprimer</Button>
                            </DialogFooter>
                        </DialogContent>
                    </Dialog>
                </div>
            </template>
        </Column>
    </div>
</template>