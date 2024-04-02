<script setup lang="ts">
import {
	Dialog,
	DialogContent,
	DialogDescription,
	DialogFooter,
	DialogHeader,
	DialogTitle,
	DialogTrigger
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"

</script>

<template>
  <div class="border border-gray-300 rounded-lg p-2 md:p-3 my-4">
    <h1 class="font-bold text-base">Evaluer la performance globale d'une équipe</h1>
    <div class="w-full pt-5 pb-2 px-0 flex justify-center">
      <Dialog>
        <DialogTrigger as-child>
          <Button class="text-white bg-primary hover:bg-primary/90">
            <span class="mx-5 my-0">Evaluer</span>
          </Button>
        </DialogTrigger>
        <DialogContent class="sm:max-w-[500px]">
          <DialogHeader>
            <DialogTitle>Evaluer la performance gloable</DialogTitle>
            <DialogDescription>
              Sélectionnez l'équipe à évaluer et indiquez la note que vous souhaitez leur accorder
            </DialogDescription>
          </DialogHeader>
          <div class="grid gap-4 py-4">
            <div class="grid grid-cols-4 items-center gap-4">
              <Label for="equipe">Equipe :</Label>
              <select id="equipe" name="equipe">
                <option value="equipe1">Equipe 1</option>
                <option value="equipe2">Equipe 2</option>
                <option value="equipe3">Equipe 3</option>
                <!-- Ajoutez plus d'options au besoin -->
              </select>
            </div>
            <div class="grid grid-cols-4 items-center gap-4">
              <Label for="note">Note :</Label>
              <Input id="note" type="gradeGlobalPerformance" min="0" max="20" />
            </div>
          </div>
          <DialogFooter>
            <Button type="submit" variant="destructive" class="text-white bg-primary hover:bg-primary/90">
              Evaluer
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  </div>
</template>

<style scoped>

</style>