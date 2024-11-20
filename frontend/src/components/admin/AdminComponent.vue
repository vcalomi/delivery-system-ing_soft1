<template>
  <div class="row w-100">
    <div class="col-12">
      <h3 class="white-text">Gestión de Productos</h3>
      <button class="btn btn-primary btn-sm mb-3" @click="createProduct">Crear Producto</button>
      <table class="table table-striped">
        <thead>
          <tr>
            <!-- <th scope="col">ID</th> -->
            <th scope="col">Nombre</th>
            <th scope="col">Stock</th>
            <th scope="col">Atributos</th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="product in products" :key="product.id">
            <!-- <td>{{ product.id }}</td> -->
            <td>{{ product.product_name }}</td>
            <td>
              <input
                type="number"
                v-model="product.stock"
                class="form-control form-control-sm"
                :min="0"
              />
              <button
                  class="btn btn-info btn-sm"
                  @click="updateStock(product)"
                >
                  incrementar stock
                </button>
            </td>
            <td>
              <div v-for="(attribute, index) in product.attributesEditable" :key="index" class="mb-1">
                <input
                  type="text"
                  v-model="product.attributesEditable[index].key"
                  placeholder="Clave"
                  class="form-control form-control-sm d-inline w-45"
                />
                <input
                  type="text"
                  v-model="product.attributesEditable[index].value"
                  placeholder="Valor"
                  class="form-control form-control-sm d-inline w-45"
                />
              </div>
              <button
                class="btn btn-primary btn-sm mt-2"
                @click="addAttribute(product)"
              >
                Agregar Atributo
              </button>
            </td>
            <td>
              <div class="btn-group">
                
                <button
                  class="btn btn-success btn-sm"
                  @click="updateAttributes(product)"
                >
                  guardar nuevo atributos
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
    };
  },
  async mounted() {
    try {
      const { data: products } = await axios.get("http://localhost:8081/api/products/", {
        headers: { Authorization: `Bearer ${localStorage.authToken}` },
      });
      // Inicializar atributos editables
      this.products = products.map((product) => ({
        ...product,
        attributesEditable: this.attributesToEditable(product.attributes),
      }));
    } catch (err) {
      console.error(err);
    }
  },
  methods: {
    // Convertir atributos de objeto a arreglo editable
    attributesToEditable(attributes) {
      return Object.entries(attributes).map(([key, value]) => ({
        key,
        value,
      }));
    },
    // Convertir atributos de arreglo editable a objeto
    editableToAttributes(attributesEditable) {
      return attributesEditable.reduce((acc, attr) => {
        if (attr.key.trim()) acc[attr.key] = attr.value.trim();
        return acc;
      }, {});
    },
    // Actualizar stock del producto
    async updateStock(product) {
      if (product.stock < 0) {
        alert("El stock no puede ser negativo.");
        return;
      }

      try {
        await axios.patch(
      `http://localhost:8081/api/products/incrementStock/${product.id}`,
      { quantity: product.stock },
      { headers: { Authorization: `Bearer ${localStorage.authToken}` } }
    );
        alert("Stock actualizado con éxito.");
      } catch (err) {
        console.error("Error al actualizar el stock:", err);
        alert("Hubo un error al actualizar el stock.");
      }
    },
    // Actualizar atributos del producto
    async updateAttributes(product) {
      const updatedAttributes = this.editableToAttributes(product.attributesEditable);

      try {
        await axios.patch(
          `http://localhost:8081/api/products/edit`,
          { id: product.id, attributes: updatedAttributes },
          { headers: { Authorization: `Bearer ${localStorage.authToken}` }
        });
        product.attributes = updatedAttributes; // Actualizar localmente
        alert("Atributos actualizados con éxito.");
      } catch (err) {
        console.error("Error al actualizar los atributos:", err);
        alert("Hubo un error al actualizar los atributos.");
      }
    },
    // Agregar un nuevo atributo vacío
    addAttribute(product) {
      product.attributesEditable.push({ key: "", value: "" });
    },
    // Navegar a la página para crear un producto
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

input.w-45 {
  width: 45%;
  display: inline-block;
  margin-right: 5px;
}

button.w-10 {
  width: 10%;
  display: inline-block;
}

.mb-1 {
  margin-bottom: 5px;
}

.white-text {
  color: rgb(220, 220, 220);
}
</style>
