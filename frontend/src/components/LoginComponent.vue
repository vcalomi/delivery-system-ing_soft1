<template>
  <Register v-if="active"/>
  <div v-else>
    <div class="login">
      <input type="text" v-model="user" class="form-control" placeholder="Username" aria-label="Username" aria-describedby="basic-addon1">
      <input type="password" v-model="password" class="form-control mt-2 mb-4" placeholder="Password" aria-label="Password" aria-describedby="basic-addon1">
      <button @click="Login()" type="button" class="btn btn-primary">Login</button>
      <h6 style="margin-top: 10px;">{{ "No estas registrado?" }}</h6>
      <RouterLink to="/register">Registrarse</RouterLink>
      
    </div>
    <RouterLink to="/forgotPassword"> forgot password?</RouterLink>
  </div>
</template>

<script>
import Register from './Register.vue';
import axios from 'axios';
export default {
  data() {
    return {
      user: "",
      password:"",
      active: false,
      forgotPasswordActive: true
    }
  },
  name: 'loginComponent',
  methods: {
    async Login() {
      await axios.post('http://localhost:8081/api/users/login', {"username": this.user, "password": this.password})
      .then( response => { localStorage.setItem('authToken', response.data); console.log(localStorage); this.$router.push({name:'Home'}) } )
      .catch( err => console.error( err ) )
    },
  },
  components:{
    Register
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #32b077;
}
</style>