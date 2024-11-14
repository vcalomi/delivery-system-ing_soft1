<template>
    <div class="change-password-container">
      <div class="change-password">
        <h4>Cambiar Contraseña</h4>
        
        <input 
          type="password" 
          v-model="oldPassword" 
          class="form-control form-control-sm mt-2" 
          placeholder="Contraseña actual" 
          aria-label="Contraseña actual" 
        />
        
        <input 
          type="password" 
          v-model="newPassword" 
          class="form-control form-control-sm mt-2" 
          placeholder="Nueva contraseña" 
          aria-label="Nueva contraseña" 
        />
        
        <input 
          type="password" 
          v-model="newPassword2" 
          class="form-control form-control-sm mt-2 mb-4" 
          placeholder="Repetir nueva contraseña" 
          aria-label="Repetir nueva contraseña" 
        />
        
        <button 
          @click="changePassword" 
          type="button" 
          class="btn btn-primary btn-sm">
          Cambiar Contraseña
        </button>
        
        <div v-if="message" class="message mt-3">{{ message }}</div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        oldPassword:"",
        newPassword:"",
        newPassword2: "",
        message:"",
      }
    },
    name: 'changePasswordComponent',
    methods: {
      async changePassword() {
        if (this.newPassword !== this.newPassword2) {
        this.message = "Las contraseñas nuevas no coinciden.";
        return;
      }
      console.log(localStorage.username);
        await axios.patch('http://localhost:8081/api/users/changePassword', {"username": localStorage.username, "oldPassword": this.oldPassword, 
                            "newPassword": this.newPassword, "repeatedNewPassword": this.newPassword2})
        .then( () => { 
          this.message = "Contraseña actualizada correctamente.";
        })
        .catch( err => {
          console.error( err )
          this.message = "Error al cambiar la contraseña. Por favor, intente nuevamente.";
        } )
      },
    },
  }
  </script>
  
  <style scoped>
  .change-password-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
  }
  
  .change-password {
    text-align: center;
    width: 100%;
    max-width: 320px;
    padding: 20px;
    background-color: #e7e7e7;
    border-radius: 8px;
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  }
  
  h4 {
    margin-bottom: 20px;
    font-size: 1.2rem;
    color: #333;
  }
  
  input.form-control {
    font-size: 0.9rem;
    padding: 0.5rem;
  }
  
  button.btn {
    font-size: 0.85rem;
    padding: 0.5rem 1rem;
  }
  
  .message {
    font-size: 0.85rem;
    color: #28a745;
  }
  </style>
  