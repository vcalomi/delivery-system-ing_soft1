import { createStore } from 'vuex'; // Use Vuex's createStore function for Vue 3

export default createStore({
  state: {
    selectedProducts: [],  // Array to hold the selected products
  },
  mutations: {
    addProduct(state, product) {
      state.selectedProducts.push(product);
    },
    removeProduct(state, productId) {
      state.selectedProducts = state.selectedProducts.filter(product => product.id !== productId);
    },
    clearProducts(state) {
      state.selectedProducts = [];
    },
  },
  actions: {
    addProductToCart({ commit }, product) {
      commit('addProduct', product);
    },
    removeProductFromCart({ commit }, productId) {
      commit('removeProduct', productId);
    },
    clearCart({ commit }) {
      commit('clearProducts');
    },
  },
  getters: {
    getSelectedProducts(state) {
      return state.selectedProducts;
    },
  },
});