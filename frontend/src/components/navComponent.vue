<template>
    <nav class="navbar navbar-light bg-dark fixed-top shadow-sm">
      <div v-if="showNavBar"  class="container-fluid">
        <RouterLink to="/" class="navbar-brand text-white" >Home</RouterLink>
        <div class="navbar-text">
          <h4 class="title-name">{{ "Bienvenido al mejor sistema de compras del mundo" }}</h4>
        </div>
        <div class="profile-menu">
          <div class="profile-info">
            <span class="role-text">{{ role }}</span>
            <img :src="profilePictureUrl || 'https://via.placeholder.com/40'" 
              alt="Profile"
              class="profile-img"
              @click="toggleProfileMenu"/>
          </div>
          <div v-if="profileMenuOpen" class="profile-dropdown">
            <ul>
              <li>
                <RouterLink to="ChangePassword">Cambiar Contraseña</RouterLink>
              </li>
              <li>
                 <RouterLink to="UploadPicture">Cambiar foto de perfil</RouterLink> 
              </li>
              <li>
                <button class="router-link-style"  @click="logout">Cerrar Sesión</button>
                </li>
            </ul>
          </div>
        </div>
      </div>
    </nav>
</template>
<script>

import axios from 'axios';

export default{
    data(){
        return {
            profileMenuOpen: false,
            role: '', 
            profile: [],
            profilePictureUrl: '',
            showNavBar: true
        }
    },
    async mounted(){
        if (!localStorage.authToken) {
        console.warn("No hay token de autenticación.");
        this.showNavBar = false; 
        }
        await axios.get('http://localhost:8081/api/users/profile', {
        headers: { Authorization: `Bearer ${localStorage.authToken}` }
        }).then(res => {
        this.profile = res.data;
        }).catch(err => alert(err));

        await axios.get("http://localhost:8081/api/users/profilePicture", {
        headers: { Authorization: `Bearer ${localStorage.authToken}` }
        }).then(res => {
          var base64Image = res.data; 
          this.profilePictureUrl = `data:image/jpeg;base64,${base64Image}`
        }).catch( (err) => {
          if(err.status === 404)
            this.profilePictureUrl = 'https://via.placeholder.com/40'; 
        })
        this.role = localStorage.role
    },
    methods: {
        toggleProfileMenu() {
        this.profileMenuOpen = !this.profileMenuOpen;
        },
        logout(){
            localStorage.clear();
            this.$router.push('/login');
        }
    }
}
</script>
<style>
.navbar {
  background-color: #616161 !important; 
  z-index: 1000;
}

.navbar-text h4 {
  margin: 0;
}

.container.mt-5.pt-5 {
  padding-top: 80px;
}

.profile-menu {
  position: relative;
  display: flex;
  align-items: center;
}

.profile-info {
  display: flex;
  align-items: center;
}

.role-text {
  font-size: 14px;
  color: #f8f9fa; 
  margin-right: 10px;
  text-transform: uppercase;
}

.profile-img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
}

.profile-dropdown {
  position: absolute;
  top: 50px;
  right: 0;
  background-color: #f8f9fa;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  border-radius: 4px;
  width: 200px;
  padding: 10px 0;
}

.profile-dropdown ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
}

.profile-dropdown li {
  padding: 8px 20px;
}

.profile-dropdown li a {
  text-decoration: none;
  color: #0b4f94;
  display: block;
}

.profile-dropdown li a:hover {
  background-color: #007bff;
  color: white;
}

.router-link-style {
  color: #f6481d; 
  text-decoration: none;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0; 
}

.title-name {
  color: white;
  font-size: 20px;
}

</style>