<template>
    <p>Orders User Component</p>
    <!-- listar las ordenes del ususario correspondiente -->
</template>
<script>
export default{
    name: OrdersUserComponent
    //this component has to be redirected by ProductsComponent but it depends on user/admin role
    //uri localhost:8081/api/orders/
    //este componenete esta dentro de UserComponent
    
    // await axios.get('http://localhost:8081/api/orders/', {
    //   headers: { Authorization: `Bearer ${localStorage.authToken}` }
    // }).then(res => {
    //   this.orders = res.data;
    // }).catch(err => console.error(err));
}
</script>
