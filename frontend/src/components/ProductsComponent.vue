<template>
    <p v-for=" p in productos" :key="p.id">{{p.product_name}}</p>
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
        await axios.get('http://localhost:8081/api/products/', {})
            .then( res => console.log(res.data)
            .catch( err => console.error(err))
            
    },
    
    methods:{
        async createProducts(){
            let token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huIn0.9Yg6ujs_hfsVQ2Xk1h7lRJ4wkWb2vGhWha8tBd_ko9k'
            await axios.post('http://localhost:8081/api/products/create', {product_name: "product_1", stock: 20 }, {
             headers: {Authorizaiton: "Bearer " + token } 
            })
            .then( res => console.log(res))
            .catch( err => console.error(err))
        }
    }
}
</script>