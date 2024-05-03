<script setup lang="ts">
import { ref, reactive } from "vue"
import AddSprintDialog from "./AddSprintDialog.vue"
import { CalendarPlus } from "lucide-vue-next"

const nbSprints = ref(0)
const IS_SPRINT = "Cliquez-ici pour ajouter un sprint"
const ISNT_SPRINT = "Vous n'avez pas encore crée de sprint, cliquez-ici pour ajouter le premier"
    
const emitAdd = defineEmits(["add:sprint"])

</script>

<template>
    <div class="border border-gray-300 border-dashed rounded-lg p-2 md:p-3 my-4 hover:bg-gray-100">   
        <AddSprintDialog @add:sprint="emitAdd('add:sprint')">
            <CalendarPlus />
            <p>{{ nbSprints != 0 ? IS_SPRINT : ISNT_SPRINT }}</p>
        </AddSprintDialog>
    </div>
</template>