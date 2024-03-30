<script setup lang="ts">
import { Button } from "@/components/ui/button"
import { ExternalLink } from "lucide-vue-next"
</script>

<template>
    <div class="border border-gray-300 rounded-lg p-2 md:p-3 my-4">
        <h1 class="font-bold text-base">Vous n'avez pas encore importé les étudiants</h1>
        <p class="text-gray-400 text-sm">Pour générer les équipes, il faut commencer par importer les étudiants.</p>
        <div class="w-full pt-5 pb-2 px-0 flex justify-center">
            <RouterLink to="/students">
                <Button class="text-white bg-primary hover:bg-primary/90 flex items-center">
                    <ExternalLink class="w-4 h-4 mr-2" />
					Importer les étudiants
                </Button>
            </RouterLink>
        </div>
    </div>
</template>