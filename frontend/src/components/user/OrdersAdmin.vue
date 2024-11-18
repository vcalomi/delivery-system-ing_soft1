<template>
  <div class="row w-100">
    <!-- Columna izquierda con lista de pedidos -->
    <div class="col-md-3 d-flex flex-column align-items-start vh-100">
      <h5>Pedidos</h5>
      <ul class="list-unstyled">
        <li v-for="(order, index) in orders" :key="index">
          <strong>Pedido #{{ order.orderId }}</strong>
          <ul>
            <li v-for="(item, itemIndex) in order.items" :key="itemIndex">
              {{ item.product_name }} - Cantidad: {{ item.quantity }}
            </li>
          </ul>
          <div class="d-flex justify-content-between mt-2">
            <!-- Botones de en proceso y enviado -->
            <button class="btn btn-success small-button" @click="inProcessOrder(order.orderId)">En proceso</button>
            <button class="btn btn-danger small-button" @click="sentOrder(order.orderId)">Enviado</button>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'OrdersAdmin',
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
    async inProcessOrder(orderId) {
      try {
        const response = await axios.patch(`http://localhost:8081/api/orders/changeStatus/${orderId}`,{ordenStatus: "in_process"},  {
          headers: { Authorization: `Bearer ${localStorage.authToken}` }
        });
        // Eliminamos el pedido de la lista
        this.orders = this.orders.filter(order => order.orderId !== orderId);
        console.log('Pedido cancelado:', response.data);
      } catch (error) {
        console.error('Error cancelando pedido:', error);
      }
    },
    async sentOrder(orderId) {
      try {
        const response = await axios.patch(`http://localhost:8081/api/orders/changeStatus/${orderId}`,{ordenStatus: "sent"},  {
          headers: { Authorization: `Bearer ${localStorage.authToken}` }
        });
        // Eliminamos el pedido de la lista
        this.orders = this.orders.filter(order => order.orderId !== orderId);
        console.log('Pedido cancelado:', response.data);
      } catch (error) {
        console.error('Error cancelando pedido:', error);
      }
    }
  }
};
</script>

<style scoped>
/* Estilo para los botones */
.btn {
  font-size: 0.875rem;
  padding: 0.375rem 0.75rem;
}

.small-button {
  font-size: 0.75rem; /* Reduce el tamaño de la fuente */
  padding: 2px 8px; /* Reduce el padding */
}

/* Estilo de los botones: Confirmar (verde) y Cancelar (rojo) */
.btn-success {
  background-color: #28a745; /* Verde */
  border-color: #28a745;
}

.btn-success:hover {
  background-color: #218838;
  border-color: #1e7e34;
}

.btn-danger {
  background-color: #dc3545; /* Rojo */
  border-color: #dc3545;
}

.btn-danger:hover {
  background-color: #c82333;
  border-color: #bd2130;
}

/* Agregar algo de espacio entre botones */
.d-flex.justify-content-between {
  margin-top: 1rem;
}

/* Estilo de la lista de pedidos */
.list-unstyled {
  padding-left: 0;
}

li {
  margin-bottom: 1rem;
}

h5 {
  font-weight: bold;
  margin-bottom: 1rem;
}

/* Asegura que la lista ocupe toda la altura disponible */
.vh-100 {
  min-height: 100vh;
}

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
