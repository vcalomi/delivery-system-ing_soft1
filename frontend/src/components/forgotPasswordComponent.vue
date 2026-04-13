<template>
    <div class="modal-content">
        <h5>Recuperar contraseña</h5>
        <input 
          type="text" 
          v-model="user" 
          class="form-control form-control-sm" 
          placeholder="Introduce tu usuario" 
          aria-label="Username">
        <button 
          @click="recoverPassword()" 
          type="button" 
          class="btn btn-primary btn-sm mt-2">
          Recuperar por mail
        </button>
    </div>
</template>

<script>
import axios from 'axios';
import { API_BASE_URL } from '@/apiConfig';
export default {
    name: 'forgotPasswordComponent',
    data() {
        return {
            user: "",
            forgotUsername: ""
        }
    },
    methods: {
        recoverPassword(){ 
            axios.patch(`${API_BASE_URL}/api/users/forgetPassword`, { username: this.user })
                .then(response => { 
                    console.log("Recovery email sent to", this.forgotUsername)
                    this.user = ""
                    console.log(response)
                })
                .catch(err => console.error(err));
        }
    }
}
</script>
