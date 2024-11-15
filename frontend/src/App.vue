<template>
  
    <nav class="navbar navbar-light bg-dark fixed-top shadow-sm">
      <div class="container-fluid">
        <RouterLink to="/" class="navbar-brand">Home</RouterLink>
        <div class="navbar-text">
          <h4>{{ "Bienvenido al mejor sistema de compras del mundo" }}</h4>
        </div>
        <div class="profile-menu">
          <div class="profile-info">
            <span class="role-text">{{ role }}</span>
            <img
              src="https://via.placeholder.com/40" 
              alt="Profile"
              class="profile-img"
              @click="toggleProfileMenu"
            />
          </div>
          <div v-if="profileMenuOpen" class="profile-dropdown">
            <ul>
              <li>
                <RouterLink to="ChangePassword">Cambiar Contraseña</RouterLink>
              </li>
              <li>
                <RouterLink to="UserOrders">Pedidos</RouterLink>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </nav>

    <div class="container-fluid mt-3 pt-5 mh-100">
      <RouterView></RouterView>
    </div>

</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      profileMenuOpen: false,
      role: 'ADMIN', // Rol
    };
  },
  methods: {
    toggleProfileMenu() {
      this.profileMenuOpen = !this.profileMenuOpen;
    },
  },
};
</script>

<style scoped>
#app {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #0b4f94;
}

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
</style>
