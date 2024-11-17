<template>
  <div class="row w-100">
    <div class="col-12">
      <h3>Gestión de Productos</h3>
      <button class="btn btn-primary btn-sm mb-3" @click="createProduct">Crear Producto</button>
      <table class="table table-striped">
        <thead>
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Nombre</th>
            <th scope="col">Stock</th>
            <th scope="col">Atributos</th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="product in products" :key="product.id">
            <td>{{ product.id }}</td>
            <td>{{ product.product_name }}</td>
            <td>
              <input
                type="number"
                v-model="product.stock"
                class="form-control form-control-sm"
                :min="0"
              />
            </td>
            <td>
              <input
                type="text"
                v-model="products.attributes"
                class="form-control form-control-sm"
              />
            </td>
            <td>
              <div class="btn-group">
                <button
                  class="btn btn-info btn-sm"
                  @click="updateStock(product)"
                >
                  Guardar Stock
                </button>
                <button
                  class="btn btn-success btn-sm"
                  @click="updateAttributes(product)"
                >
                  Guardar Atributos
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "adminComponent",
  data() {
    return {
      products: [],
      orders: []
    };
  },
  async mounted(){
    await axios.get('http://localhost:8081/api/products/', {
      headers: { Authorization: `Bearer ${localStorage.authToken}` }
    }).then(res => {
      this.products = res.data;
    }).catch(err => console.error(err));
    // /orders/all?sortBy=confirmed
    await axios.get('http://localhost:8081/api/orders/all', {
      headers: { Authorization: `Bearer ${localStorage.authToken}` }
    }).then(res => {
      this.orders = res.data;
    }).catch(err => console.error(err));
  },
  methods: {
    updateStock(product) {
      if (product.stock < 0) {
        alert("El stock no puede ser negativo.");
        return;
      }

      axios
        .patch(
          `http://localhost:8081/api/products/incrementStock/${product.id}`,
          { incrementBy: product.stock },
          { headers: { Authorization: `Bearer ${localStorage.authToken}` } }
        )
        .then(() => {
          alert("Stock actualizado con éxito.");
        })
        .catch((err) => {
          console.error("Error al actualizar el stock:", err);
          alert("Hubo un error al actualizar el stock.");
        });
    },
    updateAttributes(product) {
      if (!product.atribute.trim()) {
        alert("La descripción no puede estar vacía.");
        return;
      }

      axios.patch(
          `http://localhost:8081/api/products/edit`,
          {
            id: product.id,
            atribute: product.atribute,
          },
          { headers: { Authorization: `Bearer ${localStorage.authToken}` } }
        )
        .then(() => {
          alert("Atributos actualizados con éxito.");
        })
        .catch((err) => {
          console.error("Error al actualizar los atributos:", err);
          alert("Hubo un error al actualizar los atributos.");
        });
    },
    createProduct() {
      this.$router.push({ name: "CreateProductComponent" });
    },
  },
};
</script>

<style scoped>
.table {
  margin-top: 20px;
}

.btn-sm {
  font-size: 0.75rem;
  padding: 5px 10px;
}

.btn-group {
  display: flex;
  gap: 5px;
}
</style>
