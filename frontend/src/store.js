import { createStore } from 'vuex'; 

export default createStore({
  state: {
    selectedProducts: [],
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