import { createRouter, createWebHistory } from 'vue-router'
import ProductsComponent from './components/ProductsComponent.vue'
import forgotPasswordComponent from './components/forgotPasswordComponent.vue'
import Register from './components/Register.vue'
import LoginComponent from './components/LoginComponent.vue'
import CreateProductComponent from './components/CreateProductComponent.vue'
import CreateOrderComponent from './components/CreateOrderComponent.vue'
import ChangePasswordComponent from './components/ChangePasswordComponent.vue'
import UserComponent from './components/UserComponent.vue'

// Define the routes
const routes = [
  {
    path: '/',
    name: 'Home',
    component: ProductsComponent
  },
  {
    path: '/forgotPassword',
    name: 'forgotPasswordComponent',
    component: forgotPasswordComponent
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginComponent
  },
  {
    path: '/createProduct',
    name: 'CreateProductComponent',
    component: CreateProductComponent
  },
  {
    path: '/createOrder',
    name: 'CreateOrderComponent',
    component: CreateOrderComponent
  },
  {
    path: '/changePassword',
    name: 'ChangePasswordComponent',
    component: ChangePasswordComponent
  },
  {
    path: '/userHome',
    name: 'UserHome',
    component: UserComponent
  }

  // Add more routes here
]

// Create the router instance
const router = createRouter({
  history: createWebHistory(),  // Uses HTML5 History mode
  routes  // The array of routes defined above
})


router.beforeEach((to, from, next) => {
  // Check if the route requires authentication
  // tiene que ser bearer token en vez de authToken
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('authToken') || sessionStorage.getItem('authToken'); // Check for token in localStorage or sessionStorage

    // If no token is found, redirect to login page
    if (!token) {
      next({ name: 'Login' });
    } else {
      next(); // Allow the navigation
    }
  } else {
    next(); // If no authentication is required, just allow the navigation
  }
});


export default router