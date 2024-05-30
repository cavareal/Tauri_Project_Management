<script setup lang="ts">
import { ref, watch } from "vue"
import { Column } from "@/components/atoms/containers"
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { RoleTypeSchema, type RoleType } from "@/types/role"
import { createUser } from "@/services/user/user.service"
import { useMutation } from "@tanstack/vue-query"

const userRole = ref<RoleType>();
const userMail = ref<string>("");
const userName = ref<string>("");

watch(userMail, (newMail) => {
  if (newMail) {
    const [firstName, lastNameWithDomain] = newMail.split('@')[0].split('.');
    if (firstName && lastNameWithDomain) {
      const lastName = lastNameWithDomain.toUpperCase();
      const formattedFirstName = firstName.charAt(0).toUpperCase() + firstName.slice(1);
      userName.value = `${lastName} ${formattedFirstName}`;
    }
  }
});



const { error, isPending, mutate } = useMutation({ mutationFn: async() => {

  if (!userMail.value || !userRole.value || !userName.value) {
    alert("Veuillez remplir tous les champs");
    return
  }

	await createUser(userMail.value, userName.value, userRole.value)
} })


</script>

<template>
    <div class="border border-gray-300 border-dashed rounded-lg hover:bg-gray-100 flex justify-center flex-col items-stretch p-4">
        <h2 class="text-xl font-semibold text-center mb-4">Ajouter un utilisateur</h2>
        <Column class="items-center gap-4">

            <div class="w-full flex flex-col items-start mb-2">
                <label for="email" class="mb-1 text-sm font-medium text-gray-700">Email</label>
                <Input id="email" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" type="text" v-model="userMail" />
            </div>

            <div class="w-full flex flex-col items-start mb-2">
                <label for="username" class="mb-1 text-sm font-medium text-gray-700">Nom d'utilisateur</label>
                <Input id="username" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" type="text" v-model="userName" />
            </div>

            <Select v-model="userRole" class="w-full">
                <SelectTrigger class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                    <SelectValue placeholder="Role" />
                </SelectTrigger>
                <SelectContent>
                    <SelectGroup>
						        <SelectItem v-for="roleType in RoleTypeSchema.options" :key="roleType" :value="roleType">
                          {{ roleType }}
                      </SelectItem>
                    </SelectGroup>
                </SelectContent>
            </Select>

            <Button class="mt-4 px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600" @click="mutate">Valider</Button>
            <p v-if=error>Erreur lors de la création d'un utilisateur</p>
        </Column>
    </div>
</template> 