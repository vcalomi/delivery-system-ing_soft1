<template>
  <Register v-if="active"/>
  <div v-else class="login-container">
    <div class="login">
      <input type="text" v-model="user" class="form-control form-control-sm" placeholder="Username" aria-label="Username" aria-describedby="basic-addon1">
      <input type="password" v-model="password" class="form-control form-control-sm mt-2 mb-4" placeholder="Password" aria-label="Password" aria-describedby="basic-addon1">
      <button @click="Login()" type="button" class="btn btn-primary btn-sm">Login</button>
      <h6 style="margin-top: 10px;">{{ "No estas registrado?" }}</h6>
      <RouterLink to="/register">Registrarse</RouterLink>
      <div class="forgot-password">
      <RouterLink to="/forgotPassword" class="btn-link">Forgot password?</RouterLink>
    </div>
    </div>
  
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
      .then( response => { 
        localStorage.setItem('authToken', response.data); 
        localStorage.setItem('username', this.user);
        console.log(localStorage); 
        this.$router.push({name:'Home'});
      })
      .catch( err => console.error( err ) )
    },
  },
  components: {
    Register
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.login {
  text-align: center;
  width: 100%;
  max-width: 320px;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}

input.form-control {
  font-size: 0.9rem;
  padding: 0.5rem;
}

button.btn {
  font-size: 0.8rem;
  padding: 0.5rem 1rem;
}

h6 {
  font-size: 0.9rem;
}

.forgot-password {
  text-align: center;
  margin-top: 15px;
}

.forgot-password .btn-link {
  font-size: 0.85rem;
  color: #32b077;
}

a {
  color: #32b077;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}
</style>
