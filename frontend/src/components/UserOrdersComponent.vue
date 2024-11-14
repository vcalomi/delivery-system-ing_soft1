<template>
  <div class="orders-container">
    <div class="orders">
      <h4>Pedidos Confirmados</h4>
      
      <button 
        @click="fetchConfirmedOrders" 
        type="button" 
        class="btn btn-primary btn-sm mb-3">
        Cargar Pedidos
      </button>
      
      <ul v-if="orders.length > 0">
        <li v-for="(order, index) in orders" :key="index">
          Pedido ID: {{ order.id }} - {{ order.description }}
        </li>
      </ul>
      
      <div v-else class="message">No hay pedidos confirmados.</div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      orders: [],  // Almacena la lista de pedidos
      message: "",
    }
  },
  name: 'UserOrdersComponent',
  methods: {
    async fetchConfirmedOrders() {
      try {
        const response = await axios.get('http://localhost:8081/api/users/orders/confirmed');
        this.orders = response.data; // Almacena los pedidos en la variable
        if (this.orders.length === 0) {
          this.message = "No hay pedidos confirmados.";
        }
      } catch (error) {
        console.error(error);
        this.message = "Error al cargar los pedidos. Por favor, intente nuevamente.";
      }
    }
  }
}
</script>

<style scoped>
.orders-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.orders {
  text-align: center;
  width: 100%;
  max-width: 320px;
  padding: 20px;
  background-color: #e7e7e7;
  border-radius: 8px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}

h4 {
  margin-bottom: 20px;
  font-size: 1.2rem;
  color: #333;
}

button.btn {
  font-size: 0.85rem;
  padding: 0.5rem 1rem;
}

ul {
  list-style: none;
  padding: 0;
  font-size: 0.9rem;
}

li {
  margin-bottom: 5px;
}

.message {
  font-size: 0.85rem;
  color: #dc3545;
}
</style>
