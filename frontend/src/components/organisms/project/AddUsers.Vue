<script setup lang="ts">
import { ref } from "vue"
import { Column } from "@/components/atoms/containers"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Input } from "@/components/ui/input"

const roles = ref<string[]>(["Etudiant", "string2", "string3"]);
const userRole = ref<string>("");
const userMail = ref<string>("");

</script>

<template>
    <div class="border border-gray-300 border-dashed rounded-lg hover:bg-gray-100 flex justify-center flex-col items-stretch p-4">
        <Column class="items-center gap-4">

            <div class="w-full flex flex-col items-start mb-2">
                <label for="email" class="mb-1 text-sm font-medium text-gray-700">Email</label>
                <Input id="email" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" type="text" v-model="userMail" />
            </div>

            <Select v-model="userRole" class="w-full">
                <SelectTrigger class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                    <SelectValue placeholder="Role" />
                </SelectTrigger>
                <SelectContent>
                    <SelectGroup>
                        <SelectItem v-for="role in roles" :key="role" :value="role">
                            {{ role }}
                        </SelectItem>
                    </SelectGroup>
                </SelectContent>
            </Select>
        </Column>
    </div>
</template>
