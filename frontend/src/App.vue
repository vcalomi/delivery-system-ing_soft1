<template>
  <HelloWorld msg="Welcome to Your Vue.js App"/>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'

export default {
  name: 'App',
  components: {
    HelloWorld
  }
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
