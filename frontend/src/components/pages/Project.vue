<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { SidebarTemplate } from '@/components/templates';
import { Header } from '@/components/molecules/header';
import { Column } from '@/components/atoms/containers';
import { hasPermission } from '@/services/user/user.service';
import { NotAuthorized } from '@/components/organisms/errors';
import AddUser from './../organisms/project/AddUser.vue';
import ManageUser from './../organisms/project/ManageUser.vue';
import { getAllUsers } from '@/services/user/user.service';
import { getAllRoles } from '@/services/role/role.service';
import { type User } from '@/types/user';
import { type Role, type RoleType } from '@/types/role';

const users = ref<User[]>([]);
const roles = ref<Role[]>([]);
const usersLoading = ref(true);
const rolesLoading = ref(true);
const usersError = ref(false);
const rolesError = ref(false);

onMounted(async () => {
	refetch()
});

async function refetch (){
	try {
    users.value = await getAllUsers();
  } catch (error) {
    usersError.value = true;
  } finally {
    usersLoading.value = false;
	usersError.value = false;
  }

  try {
    roles.value = await getAllRoles();
  } catch (error) {
    rolesError.value = true;
  } finally {
    rolesLoading.value = false;
    rolesError.value = false;
  }
}



const combinedData = computed(() => {
  return users.value.map(user => ({
    ...user,
    role: getListOfRoles(user)
  }));
});

function getListOfRoles(user: User) {
  const rolesOfUser: RoleType[] = [];
  roles.value.forEach(role => {
    if (role.user.id === user.id) {
      rolesOfUser.push(role.type);
    }
  });
  return rolesOfUser;
}

</script>

<template>
	<SidebarTemplate>
	  <NotAuthorized v-if="!hasPermission('MANAGE_PROJECT')" />
	  <Column v-else class="gap-4">
		<Header title="Gestion de projet"></Header>
		<Column>
		  <AddUser v-if="hasPermission('ADD_USER')" @add:user="refetch" />
		  <ManageUser
			v-if="hasPermission('DELETE_USER')"
			:users="combinedData"
			:users-loading="usersLoading"
			:users-error="usersError"
			:roles-loading="rolesLoading"
			:roles-error="rolesError"
			@delete:user="refetch"
		  />
		</Column>
	  </Column>
	</SidebarTemplate>
  </template>
  