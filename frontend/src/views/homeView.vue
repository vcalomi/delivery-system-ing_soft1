<template>
    <navComponent />
    <AdminComponent v-if="isAdmin"/>
    <UserComponent v-else />
</template>
<script>
    import AdminComponent from '@/components/admin/AdminComponent.vue'
    import UserComponent from '@/components/user/UserComponent.vue'
    import navComponent from '../components/navComponent.vue';
    export default {
        components:{
            AdminComponent,
            UserComponent,
            navComponent
        },computed:{ 
            isAdmin(){ return localStorage.role === 'ADMIN'}
        }

    }

</script>