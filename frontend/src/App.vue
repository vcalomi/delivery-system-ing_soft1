<template>
    <div class="container-fluid mt-3 pt-5 mh-100">
      <RouterView></RouterView>
    </div>

</template>

<script>

export default {
  name: 'App',
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

</style>
