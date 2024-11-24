<template>
  <div class="row w-100">
    <!-- lista de pedidos -->
    <div class="col-md-3 d-flex flex-column align-items-start vh-100">
      <h4 style="color: white;">Pedidos</h4>
      <ul class="list-unstyled">
        <li v-for="(order, index) in orders" :key="index">
          <strong>Pedido #{{ order.orderId }}</strong>
          <ul>
            <li v-for="(product, index) in order.products" :key="index">
              <strong>{{ product.product_name }}</strong> - Cantidad: {{ product.quantity }} - Estado: {{ order.orderStatus }}
            </li>
          </ul>
          <div class="d-flex justify-content-between mt-2">
            <!-- en proceso y enviado -->
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
      orden: [],
      selectedQuantities: {},
      orders: []  
    };
  },
  async mounted() {

    await axios.get('http://localhost:8081/api/orders/all', {
      headers: { Authorization: `Bearer ${localStorage.authToken}` }
    }).then(res => {
      this.orders = res.data;
    }).catch(err => console.error(err));
  },
  methods: {
    async inProcessOrder(orderId) {
      try {
        const response = await axios.patch(`http://localhost:8081/api/orders/changeStatus/${orderId}`,{"orderStatus": "IN_PROCESS"},  {
          headers: { Authorization: `Bearer ${localStorage.authToken}` }
        });
        this.orders = this.orders.filter(order => order.orderId !== orderId);
        console.log('Pedido en proceso:', response.data);
      } catch (error) {
        console.error('Error pedido en proceso:', error);
      }
    },
    async sentOrder(orderId) {
      try {
        const response = await axios.patch(`http://localhost:8081/api/orders/changeStatus/${orderId}`,{"orderStatus": "SENT"},  {
          headers: { Authorization: `Bearer ${localStorage.authToken}` }
        });
        this.orders = this.orders.filter(order => order.orderId !== orderId);
        console.log('Pedido enviado:', response.data);
      } catch (error) {
        console.error('Error enviando pedido:', error);
      }
    }
  }
};
</script>

<style scoped>
.btn {
  font-size: 0.875rem;
  padding: 0.375rem 0.75rem;
}

.small-button {
  font-size: 0.75rem;
  padding: 2px 8px;
}

.btn-success {
  background-color: #28a745;
  border-color: #47b160;
}

.btn-success:hover {
  background-color: #218838;
  border-color: #1e7e34;
}

.btn-danger {
  background-color: #3535b6;
  border-color: #4e35dc;
}

.btn-danger:hover {
  background-color: #7823c8;
  border-color: #ad21bd;
}

.d-flex.justify-content-between {
  margin-top: 1rem;
}

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

.small-button {
  font-size: 0.75rem;
  padding: 2px 8px;
}

.list-unstyled li {
  color: white;
} 
</style>
