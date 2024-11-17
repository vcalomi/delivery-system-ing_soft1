<template>
  <div class="row w-100">
    <!-- Columna izquierda con lista de pedidos confirmados -->
    <div class="col-md-3 d-flex flex-column align-items-start vh-100">
      <h5>Pedidos Confirmados</h5>
      <ul class="list-unstyled">
        <li v-for="(order, index) in orders" :key="index">
          <strong>Pedido #{{ order.orderId }}</strong>
          <ul>
            <li v-for="(item, itemIndex) in order.items" :key="itemIndex">
              {{ item.product_name }} - Cantidad: {{ item.quantity }}
            </li>
        <div class="d-flex gap-2 mt-2">
          <button 
            class="btn btn-danger btn-sm"
            @click="cancelOrder(order.orderId)"
          >
            Cancelar
          </button>
          <button 
            class="btn btn-success btn-sm"
            @click="confirmOrder(order.orderId)"
          >
            Confirmar
          </button>
        </div>
          </ul>
        </li>
      </ul>
    </div>

    <!-- Columna del medio con la lista de productos -->
    <div class="col-md-6">
      <div class="product-list-container">
        <div class="row">
          <div 
            v-for="(product, key) in products" 
            :key="product.id" 
            class="col-sm-6 col-md-4 col-lg-3 mb-3"
          >
            <div class="card h-100">
              <div class="card-body p-2">
                <h6 class="card-title text-center">{{ product.product_name }}</h6>
                <p class="card-text text-center">Stock: {{ product.stock }}</p>

                <label for="quantity">Cantidad:</label>
                <input 
                  v-model="selectedQuantities[key]" 
                  :disabled="product.stock <= 0"
                  id="quantity"
                  type="number"
                  class="form-control form-control-sm mb-3"
                  min="1"
                  :max="product.stock"
                />

                <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                  <button 
                    class="btn btn-primary btn-xs me-md-2 small-button" 
                    :disabled="product.stock <= 0 || selectedQuantities[key] <= 0" 
                    @click="addProduct(product, selectedQuantities[key])"
                  >
                    {{ product.stock > 0 ? 'Agregar al pedido' : 'No disponible' }}
                  </button>

                  <button 
                    class="btn btn-secondary btn-xs small-button" 
                    @click="product.showDetails = !product.showDetails"
                  >
                    {{ product.showDetails ? 'Ocultar Detalles' : 'Mostrar Detalles' }}
                  </button>
                </div>

                <div v-if="product.showDetails" class="product-details mt-3">
                  <h6>Detalles:</h6>
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
    </div>

    <!-- Columna derecha con el carrito -->
    <div class="col-md-3 cart-container">
      <h5>Carrito</h5>
      <ul>
        <li v-for="(item, index) in orden" :key="index">
          {{ item.product.product_name }} - {{ selectedQuantities[item.productId] }}
        </li>
      </ul>
      <div v-if="products" class="d-flex justify-content-center mt-4">
        <RouterLink to="/createOrder" class="btn btn-primary btn-sm">Confirmar Pedido</RouterLink>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'HomeComponent',
  data() {
    return {
      products: [],
      orden: [],
      selectedQuantities: {},
      orders: []  
    };
  },
  async mounted() {
    await axios.get('http://localhost:8081/api/products/', {
      headers: { Authorization: `Bearer ${localStorage.authToken}` }
    }).then(res => {
      this.products = res.data;
    }).catch(err => console.error(err));

    await axios.get('http://localhost:8081/api/orders/', {
      headers: { Authorization: `Bearer ${localStorage.authToken}` }
    }).then(res => {
      this.orders = res.data;
    }).catch(err => console.error(err));
  },
  methods: {
    addProduct(product, quantity) {
      var prod = { 
        "product_name": product.product_name,
        "id": product.id,
        "attributes": product.attributes,
        "quantity": quantity 
      };
      console.log(prod);
      this.orden.push(prod);
      this.$store.dispatch('addProductToCart', prod);
    },
    async cancelOrder(orderId) {
      await axios
        .delete(
          `http://localhost:8081/api/products/cancel/${orderId}`,
          { headers: { Authorization: `Bearer ${localStorage.authToken}` } }
        )
        .then(() => {
          alert("Orden cancelada correctamente.");
        })
        .catch((err) => {
          console.error("Error al cancelar orden", err);
          alert("Hubo un error al cancelar la stock.");
        });
    },
    confirmOrder(orderId) {
      axios
        .patch(
          `http://localhost:8081/api/products/confirm/${orderId}`,
          { headers: { Authorization: `Bearer ${localStorage.authToken}` } }
        )
        .then(() => {
          alert("Orden confirmada correctamente.");
        })
        .catch((err) => {
          console.error("Error al confirmar la orden", err);
          alert("Hubo un error al confirmar la stock.");
        });
    }
  }
};
</script>

<style scoped>
.product-list-container {
  width: 100%;
}

.cart-container {
  background-color: #f8f9fa;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}

.cart-container h5 {
  text-align: center;
}

.product-details {
  padding: 10px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 10px;
}

::-webkit-scrollbar-thumb:hover {
  background: #555;
}

.d-flex.justify-content-start {
  position: absolute;
  top: 80px;
  left: 20px;
}

.product-list-container {
  max-width: 100%;
}

.cart-container {
  border: 1px solid #ddd;
  padding: 20px;
  border-radius: 5px;
  background-color: #f9f9f9;
}

/* Estilo personalizado para botones pequeños */
.small-button {
  font-size: 0.75rem; /* Reduce el tamaño de la fuente */
  padding: 2px 8px; /* Reduce el padding */
}
</style>
