<template>
  <!-- <HelloWorld msg="Welcome to Your Vue.js App"/>-->
  <!--<ProductsComponent  /> -->
  
    <nav>
      <RouterLink to="/"> Home </RouterLink> |
      <RouterLink to="/login"> Login </RouterLink>
    </nav>
    <div class="hello msg">
      <h1>{{ "Bienvenido al mejor sistema de compras del mundo" }}</h1>
    </div>
    <RouterView class="vh-75"></RouterView>
  
  
</template>

<script>

export default {
  name: 'App',
}
</script>

<style>
#app {
  height: 100vh; /* Asegura que el div principal ocupe el 100% de la altura */
  display: flex; /* Permite centrar contenido con flexbox */
  flex-direction: column; /* Apila los elementos verticalmente */
  justify-content: center; /* Centra verticalmente */
  align-items: center; /* Centra horizontalmente */
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #0b4f94;
}
</style>
