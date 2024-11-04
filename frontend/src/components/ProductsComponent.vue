<template>
    <ul  v-for="p in productos" :key="p.id">
        <li v-if="p">product name: {{p.product_name}}, stock: {{ p.stock }}</li>

    </ul>
    
    <button @click="getProducts">button</button>
</template>
<script>
import axios from 'axios';


export default{
    name: 'ProductsComponent',
    data(){
        return{
            productos: []
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
        }
    }
}
</script>