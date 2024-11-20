<template>
    <div>
      <h2 style="color: white;">Orden</h2>
      <ul>
        <li style="color: white;" v-for="product in selectedProducts" :key="product.id">
          {{ product.product_name }}
          {{ " - cantidad: " + product.quantity }}
        </li>
      </ul>
      <button @click="clearCart" >Limpiar carrito</button>
      <button @click="acceptOrder" class="btn btn-primary">Aceptar orden</button>
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
        console.log("selected" + this.selectedProducts)
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

