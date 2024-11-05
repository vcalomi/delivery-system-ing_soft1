<template>
    <!-- <ul  v-for="p in productos" :key="p.id">
        <li v-if="p">product name: {{p.product_name}}, stock: {{ p.stock }}</li>

    </ul>
    
    <button @click="getProducts">button</button> -->

    <div>
    <h2>Product list</h2>
    <ul>
      <li v-for="product in products" :key="product.id">
        <h3>{{ product.product_name }}</h3>
        <p>Stock: {{ product.stock }}</p>
        
        <!-- Input para la cantidad -->
        <input 
          type="number" 
          v-model.number="product.stock" 
          min="1" 
          max="product.stock" 
          :disabled="product.stock <= 0" 
          placeholder="Cantidad" />

        <!-- Botón para agregar al pedido -->
        <button 
          :disabled="product.stock <= 0 || product.stock <= 0" 
          @click="addProduct(product)">
          {{ product.stock > 0 ? 'Agregar al pedido' : 'No disponible' }}
        </button>

        <!-- Botón para mostrar detalles del producto -->
        <button @click="product.showDetails = !product.showDetails">
          {{ product.showDetails ? 'Ocultar Detalles' : 'Mostrar Detalles' }}
        </button>

        <!-- Detalles del producto -->
        <div v-if="product.showDetails" class="product-details">
          <h4>Details:</h4>
          <ul>
            <li v-for="(detail, index) in product.details" :key="index">
              {{ detail }} 
            </li>
          </ul>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>

import axios from 'axios';

export default{
    name: 'ProductsComponent',
    data(){
        return{
            products: []
        }

    },
    async beforeMount(){
        let token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsaWNoYSJ9.7rNwjkBXI05IF8INBwQ0moNXuWPz6YGPvtQJOHTmJG4'
            console.log("Bearer " + token)
            await axios.post('http://localhost:8081/api/products/create', {product_name: "product_1", stock: 20 }, {
             headers: {Authorization: "Bearer " + token } 
            })
            .then( res => console.log(res.status))
            .catch( err => console.error(err))
    }, 
    async mounted()  {
        let token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsaWNoYSJ9.7rNwjkBXI05IF8INBwQ0moNXuWPz6YGPvtQJOHTmJG4'
        console.log("Bearer " + token)
        await axios.get('http://localhost:8081/api/products/', {}).then( res => {
            this.productos = res.data
            console.log(res.data)
        })
        .catch( err => console.error(err))
            
    },
    methods:{
        getProducts(){
            console.log(this.productos)
        },
        addProduct(product){
            console.log("add product: ", product)
        }
    }
}
</script>