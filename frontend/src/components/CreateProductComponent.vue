<template>
    <div>
        <h2> Create Product</h2>
        <input type="text" v-model="product_name" class="form-control" placeholder="Product Name" aria-label="Product Name" aria-describedby="basic-addon1">
        <input type="number" v-model="stock" class="form-control mt-2 mb-4" placeholder="Stock" aria-label="Stock" aria-describedby="basic-addon1">
        <button @click="createProduct()" type="button" class="btn btn-primary">Crear producto</button>
        <h3>{{this.response}}</h3>
    </div>
</template>
<script>
import axios from 'axios';
export default{
    name: 'CreateProductComponent',
    data(){
        return{
            product_name: "",
            stock: "",
            response: ""            
        }

    }, 
    methods:{
        async createProduct(){
            let token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsaWNoYSJ9.7rNwjkBXI05IF8INBwQ0moNXuWPz6YGPvtQJOHTmJG4'
            console.log("Bearer " + token)
            await axios.post('http://localhost:8081/api/products/create', {product_name: this.product_name, stock: this.stock }, {
             headers: {Authorization: "Bearer " + token } 
            })
            .then( res => {
                if(res.status === 201){
                this.response = 'El producto se creo con éxito'}
                else{
                    this.response = 'No se pudo crear el producto'
                }})
            .catch( err => console.error(err))
        }
    }
}
</script>