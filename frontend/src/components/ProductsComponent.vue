<template>
    <p>template</p>
</template>
<script>
import axios from 'axios';
export default{
    name: 'ProductsComponent',
    mounted:{
        async createProducts(){
            let token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huIn0.9Yg6ujs_hfsVQ2Xk1h7lRJ4wkWb2vGhWha8tBd_ko9k'
            await axios.post('http://localhost:/api/products', {product_name: "product_1", stock: 20 }, {
             headers: {Authorizaiton: "Bearer " + token } 
            })
            .then( res => console.log(res))
            .catch( err => console.error(err))
        }
    },
    methods:{
        async createProducts(){
            let token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huIn0.9Yg6ujs_hfsVQ2Xk1h7lRJ4wkWb2vGhWha8tBd_ko9k'
            await axios.post('http://localhost:/api/products', {product_name: "product_1", stock: 20 }, {
             headers: {Authorizaiton: "Bearer " + token } 
            })
            .then( res => console.log(res))
            .catch( err => console.error(err))
        }
    }
}
</script>