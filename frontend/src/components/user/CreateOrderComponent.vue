<template>
    <div>
      <h2>Order Summary</h2>
      <ul>
        <li v-for="product in selectedProducts" :key="product.id">
          {{ product.product_name }}
        </li>
      </ul>
      <button @click="clearCart" >Clear Cart</button>
      <button @click="acceptOrder" class="btn btn-primary">Accept Order</button>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  export default {
    computed: {
      selectedProducts() {
        return this.$store.getters.getSelectedProducts;  // Access selected products from Vuex
      },
    },
    methods: {
      clearCart() {
        this.$store.dispatch('clearCart');  // Clear cart (empty the selected products)
      },
      async acceptOrder(){
        await axios.post('http://localhost:8081/api/orders/create', {products: this.selectedProducts}, {
          headers: {Authorization: `Bearer ${localStorage.authToken}`}
      }).then( () => { 
          alert("Orden creada con éxito!");
          this.$router.push({name:'UserHome'});
        })
        .catch( err => {
          console.error( err )
          this.message = "Error al cambiar la contraseña. Por favor, intente nuevamente.";
        } )
      }

    },
  };
  </script>