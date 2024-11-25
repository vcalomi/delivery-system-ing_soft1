<template>
    <div class="container mt-4">
      <!-- inputs del registro -->
      <div class="input-group mb-3">        
        <input type="text" v-model="name" class="form-control" placeholder="Nombre" aria-label="Nombre" aria-describedby="basic-addon1">
      </div>
  
      <div class="input-group mb-3">
        <input type="text" v-model="lastname" class="form-control" placeholder="Apellido" aria-label="Apellido" aria-describedby="basic-addon1">
      </div>

      <div class="input-group mb-3">
        <span class="input-group-text" id="basic-addon1">@</span>
        <input type="text" v-model="user" class="form-control" placeholder="Usuario" aria-label="Usuario" aria-describedby="basic-addon1">
      </div>
  
      <div class="input-group mb-3">
        <input type="email" v-model="email" class="form-control" placeholder="Correo electrónico" aria-label="Correo electrónico" aria-describedby="basic-addon2">
      </div>
  
      <div class="input-group mb-3">
        <span class="input-group-text" id="password-addon"></span>
        <input type="password" v-model="password" class="form-control" placeholder="Contraseña" aria-label="Contraseña" aria-describedby="password-addon">
      </div>
  
      <div class="input-group mb-3">
        <span class="input-group-text"></span>
        <input type="password" v-model="confirmPassword" class="form-control" placeholder="Confirmar Contraseña" aria-label="Confirmar Contraseña">
      </div>
  
    <div class="input-group mb-3">
      <span class="input-group-text" id="birthdate-addon">Fecha de Nacimiento</span>
      <input 
        type="text" 
        v-model="birthDate" 
        class="form-control" 
        placeholder="dd/mm/yyyy"
        aria-label="Fecha de Nacimiento" 
        aria-describedby="birthdate-addon" 
        maxlength="10" 
        @input="formatBirthDate"
      >
    </div>
  
      <div class="input-group mb-3">
        <span class="input-group-text" id="gender-addon">Género</span>
        <select v-model="gender" class="form-select" aria-label="Género" aria-describedby="gender-addon">
          <option selected disabled>Selecciona tu género</option>
          <option value="male">Masculino</option>
          <option value="female">Femenino</option>
        </select>
      </div>
  
      <div class="input-group mb-3">
        <span class="input-group-text">Domicilio</span>
        <input type="text" v-model="street" class="form-control" placeholder="Calle" aria-label="calle">
        <input type="text" v-model="streetNumber" class="form-control" placeholder="Número" aria-label="numero">
        <input type="text" v-model="floorApartment" class="form-control" placeholder="Piso y Depto" aria-label="piso">
        <input type="text" v-model="postalCode" class="form-control" placeholder="Código Postal" aria-label="codigo postal">
      </div>
      <button @click="Register" type="button" class="btn btn-primary" :disabled="active">Registrar usuario</button>
      <div v-if="active" class="spinner-border" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios'
  export default {
    data() {
      return {
        name: "",
        lastname: "",
        user: "",
        email: "",
        password: "",
        confirmPassword: "",
        birthDate: "",
        gender: "",
        street: "",
        streetNumber: "",
        floorApartment: "",
        postalCode: "",
        active: false
      }
    },
    name: 'Registrer-User',
    props: {
    },
    methods: {
      checkRegister() {
       const errors = [];

        if (!this.user || this.user.length < 3) {
          errors.push("El nombre de usuario debe tener al menos 3 caracteres.");
        }

        const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!emailPattern.test(this.email)) {
          errors.push("Por favor, ingresa un correo electrónico válido.");
        }

        if (!this.password || this.password.length < 6) {
          errors.push("La contraseña debe tener al menos 6 caracteres.");
        }

        if (this.password !== this.confirmPassword) {
          errors.push("Las contraseñas no coinciden.");
        }

        if (!this.birthDate) {
          errors.push("Por favor, selecciona tu fecha de nacimiento.");
        } else {
          const birthDateObj = new Date(this.birthDate);
          const today = new Date();
          const age = today.getFullYear() - birthDateObj.getFullYear();
          if (age < 18) {
            errors.push("Debes tener al menos 18 años para registrarte.");
          }
        }
        if (!this.gender) {
          errors.push("Por favor, selecciona tu género.");
        }
        if (!this.street || !this.streetNumber) {
          errors.push("Por favor, ingresa una dirección válida (calle y número).");
        } else if (isNaN(this.streetNumber)) {
          errors.push("El número de calle debe ser un valor numérico.");
        }
        if (!this.postalCode || this.postalCode.length < 4) {
          errors.push("Por favor, ingresa un código postal válido (mínimo 4 dígitos).");
        }
        return errors;
      },
    formatBirthDate() {
      let cleaned = this.birthDate.replace(/\D/g, "");
      
      // formato dd/mm/yyyy
      if (cleaned.length > 2) {
        cleaned = cleaned.slice(0, 2) + '/' + cleaned.slice(2);
      }
      if (cleaned.length > 5) {
        cleaned = cleaned.slice(0, 5) + '/' + cleaned.slice(5, 9);
      }
      this.birthDate = cleaned;
    },
    async Register(){
      let status = this.checkRegister();
      const today = new Date();
      let splitted = this.birthDate.split('/')
      const birthDateObj = new Date(splitted[2],splitted[1]);
      let age = today.getFullYear() - birthDateObj.getFullYear();
      if(status.length === 0){
        this.active = true
          await axios.post('http://localhost:8081/api/users/register', { 
          "name": this.name,"lastname": this.lastname,"email": this.email,
          "age": age,"address": this.street+" "+this.streetNumber+" "+this.postalCode+" "+this.floorApartment,
          "username": this.user,"password": this.password, "gender": this.gender 
        })
          .then(() => {
            this.active = false
            this.$router.push({name:'HomeView'}) 
          }).catch( error => {
            console.error('Error: ', error);
          })
      }else {
        alert(status)
      }
      
    },
  }
}
</script>
  
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