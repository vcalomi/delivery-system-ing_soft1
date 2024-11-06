<template>
  <Register v-if="active"/>
  <div v-else>
    <div class="hello msg">
      <h1>{{ "Bienvenido al mejor sistema de compras del mundo" }}</h1>
    </div>
    <div class="login">
      <input type="text" v-model="user" class="form-control" placeholder="Username" aria-label="Username" aria-describedby="basic-addon1">
      <input type="password" v-model="password" class="form-control mt-2 mb-4" placeholder="Password" aria-label="Password" aria-describedby="basic-addon1">
      <button @click="Login()" type="button" class="btn btn-primary">Login</button>
      <h6 style="margin-top: 10px;">{{ "No estas registrado?" }}</h6>
      <button @click="getRegister()" type="button" class="btn btn-primary" style="margin-left: 1px;">Registrar</button>
    </div>
    <div v-if="forgotPasswordActive" class="forgot-password-modal">
      <div class="modal-content">
        <h5>Recuperar contraseña</h5>
        <input type="text" v-model="user" class="form-control" placeholder="Introduce tu usuario" aria-label="Username">
        <button @click="recoverPassword()" type="button" class="btn btn-primary mt-2">Recuperar por mail</button>
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
      forgotPasswordActive: false
    }
  },
  name: 'HelloWorld',
  props: {
    msg: String
  },
  methods: {
    async Login() {
      await axios.post('http://localhost:8081/api/users/login', {"username": user, "password": password})
      .then( response => { console.log( response )} )
      .catch( err => console.error( err ) )
    },
    getRegister(){
      this.active = true;
    },
    recoverPassword(){
      axios.post('http://localhost:8081/api/users/forgetPassword', { username: this.user })
        .then(response => { 
          console.log("Recovery email sent to", this.forgotUsername);
          this.forgotPasswordActive = false;
          this.user = "";
        })
        .catch(err => console.error(err));
    }
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