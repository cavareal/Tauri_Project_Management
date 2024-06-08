<script setup lang="ts">
import { ref, watch } from 'vue';
import { Column } from '@/components/atoms/containers';
import { Button } from '@/components/ui/button';
import { useMutation } from '@tanstack/vue-query';
import { createToast } from '@/utils/toast';
import { type User } from '@/types/user';
import { Trash } from 'lucide-vue-next';
import { deleteUser } from '@/services/user/user.service';
import { formatRole, type RoleType } from '@/types/role';
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from '@/components/ui/dialog';
import { Cookies } from "@/utils/cookie"

const props = defineProps<{
    users: Array<User & { role: string[] }>;
    usersLoading: boolean;
    usersError: any;
    rolesLoading: boolean;
    rolesError: any;
}>();


const emits = defineEmits(["delete:user"])
const userToDelete = ref<number | null>(null);
const isDialogOpen = ref<boolean>(false);
const pojectLeaderId = Cookies.getUserId()

function openDelete(userId: number) {
    userToDelete.value = userId;
    isDialogOpen.value = true;
}

function getRoleDescription(role: string[]) {
    return role.map(formatRole);
}

const { mutate: deleteUserMutate } = useMutation({
    mutationKey: ['delete-user'],
    mutationFn: async () => {
        if (userToDelete.value !== null) {
            await deleteUser(userToDelete.value)
                .then(() => {
                    createToast("L'utilisateur a été supprimé.");
                    isDialogOpen.value = false;
                    emits('delete:user')
                })
                .catch(() => {
                    createToast("Erreur lors de la suppression de l'utilisateur.");
                });
        }
    },
});

watch(isDialogOpen, (newVal) => {
    if (!newVal) userToDelete.value = null;
});
</script>

<template>
    <Column class="items-center mx-5">
        <h2 class="text-xl font-semibold text-center mt-10 mb-4">Gestion des utilisateurs</h2>
        <template v-if="usersLoading || rolesLoading">
            <div>Chargement...</div>
        </template>
        <template v-else-if="usersError || rolesError">
            <div>Erreur lors du chargement des données.</div>
        </template>
        <template v-else v-for="user in users" :key="user.id">
            <div v-if="pojectLeaderId != user.id && user.role[0] != 'OPTION_STUDENT'"
                class="flex justify-between items-center w-full p-2 border-t border-gray-300">
                <div>
                    <p class="font-medium">{{ user.name }}</p>
                    <p class="text-gray-500">{{ user.email }}</p>
                    <p class="text-gray-400">{{ getRoleDescription(user.role).join(', ') }}</p>
                </div>
                <Dialog v-model:open="isDialogOpen">
                    <DialogTrigger as-child>
                        <Button @click="openDelete(user.id)">
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
                            <Button @click="isDialogOpen = false">Annuler</Button>
                            <Button class="bg-red-500 hover:bg-red-700 text-white" @click="deleteUserMutate">
                                Supprimer
                            </Button>
                        </DialogFooter>
                    </DialogContent>
                </Dialog>
            </div>
        </template>
    </Column>
</template>
