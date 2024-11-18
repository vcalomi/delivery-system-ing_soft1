import { createRouter, createWebHistory } from 'vue-router'
import AdminComponent from './components/admin/AdminComponent.vue'
import forgotPasswordComponent from './components/forgotPasswordComponent.vue'
import Register from './components/Register.vue'
import LoginComponent from './components/LoginComponent.vue'
import CreateProductComponent from './components/admin/CreateProductComponent.vue'
import CreateOrderComponent from './components/user/CreateOrderComponent.vue'
import ChangePasswordComponent from './components/ChangePasswordComponent.vue'
import UserComponent from './components/user/UserComponent.vue'
import OrdersAdminComponent from './components/admin/OrdersAdmin.vue'
import UploadPicture from './components/UploadPicture.vue'

// Define the routes
const routes = [
  {
    path: '/',
    name: 'Home',
    component: AdminComponent
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
  },
  {
    path: '/ordersAdmin',
    name: 'ordersAdmin',
    component: OrdersAdminComponent
  },
  {
    path: '/uploadPicture',
    name: 'UploadPicture',
    component: UploadPicture
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