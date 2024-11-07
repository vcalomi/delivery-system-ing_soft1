<template>
  <div>
    <h2 class="mt-5">Product List</h2>
    
    <!-- Botón Confirmar Pedido superior -->
    <div class="d-flex justify-content-center mt-3">
      <button class="btn btn-primary">Confirmar Pedido</button>
    </div>

    <div class="container mt-5">
      <div class="row">
        <div 
          v-for="(product, key) in products" 
          :key="product.id" 
          class="col-sm-6 col-md-4 col-lg-3 mb-4"
        >
          <div class="card h-100">
            <div class="card-body">
              <h5 class="card-title">{{ product.product_name }}</h5>
              <p class="card-text">Stock: {{ product.stock }}</p>

              <label for="quantity">Cantidad:</label>
              <select 
                v-model="selectedQuantities[key]" 
                :disabled="product.stock <= 0"
                id="quantity"
                class="form-select mb-3"
              >
                <option v-for="n in product.stock + 1" :key="n" :value="n - 1">
                  {{ n - 1 }}
                </option>
              </select>

              <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                <button 
                  class="btn btn-primary me-md-2" 
                  :disabled="product.stock <= 0 || selectedQuantities[key] <= 0" 
                  @click="addProduct(product, key)"
                >
                  {{ product.stock > 0 ? 'Agregar al pedido' : 'No disponible' }}
                </button>

                <button 
                  class="btn btn-secondary" 
                  @click="product.showDetails = !product.showDetails"
                >
                  {{ product.showDetails ? 'Ocultar Detalles' : 'Mostrar Detalles' }}
                </button>
              </div>

              <div v-if="product.showDetails" class="product-details mt-3">
                <h6>Details:</h6>
                <ul>
                  <li v-for="(detail, index) in product.details" :key="index">
                    {{ detail }}
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Botón Confirmar Pedido inferior -->
    <div class="d-flex justify-content-center mt-4">
      <button class="btn btn-primary">Confirmar Pedido</button>
    </div>
  </div>
</template>


<script>

import axios from 'axios';

export default{
    name: 'HomeComponent',
    data(){
        return{
            products: [],
            orden: [],
            selectedQuantities: {} 
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
            this.products = res.data
            console.log(res.data)
        })
        .catch( err => console.error(err))
            
    },
    methods:{
        getProducts(){
            console.log(this.products)
        },
        addProduct(product, productId){
          if(this.orden.filter(p => p.id === productId ).length === 0){
            this.orden.push(product)
            console.log(this.orden)
          }            
        }
    }
}
</script>

<style scoped>
h2 {
  text-align: center;
  margin-bottom: 20px;
}

.product-details {
  padding: 10px;
  background-color: #f8f9fa;
  border-radius: 4px;
}
</style>