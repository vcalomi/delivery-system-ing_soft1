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
//import { jwtDecode } from 'jwt-decode'

// Define the routes
const routes = [
  {
    path: '/',
    name: 'Home',
    component: AdminComponent,
    meta: { 
      requiresAuth: true,
      /*roles: [ 'ADMIN']*/
     }
  },
  {
    path: '/forgotPassword',
    name: 'forgotPasswordComponent',
    component: forgotPasswordComponent,
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: { requiresAuth: false  }
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginComponent,
    meta: { requiresAuth: false }
  },
  {
    path: '/createProduct',
    name: 'CreateProductComponent',
    component: CreateProductComponent,
    meta: { requiresAuth: true, /*roles: ['ADMIN'] */ }
  },
  {
    path: '/createOrder',
    name: 'CreateOrderComponent',
    component: CreateOrderComponent,
    meta: { requiresAuth: true, /*roles: ['USER']*/ }
  },
  {
    path: '/changePassword',
    name: 'ChangePasswordComponent',
    component: ChangePasswordComponent,
    meta: { requiresAuth: true, /*roles: ['ADMIN', 'USER']*/ }
  },
  {
    path: '/userHome',
    name: 'UserHome',
    component: UserComponent,
    meta: { requiresAuth: true, /*roles: [ 'USER']*/ }
  },
  {
    path: '/ordersAdmin',
    name: 'ordersAdmin',
    component: OrdersAdminComponent,
    meta: { requiresAuth: true, /*roles: ['ADMIN'] */}
  },
  {
    path: '/uploadPicture',
    name: 'UploadPicture',
    component: UploadPicture,
    meta: { requiresAuth: true, /*roles: ['ADMIN', 'USER']*/ }
  }

  // Add more routes here
]

// Create the router instance
const router = createRouter({
  history: createWebHistory(),  // Uses HTML5 History mode
  routes  // The array of routes defined above
})



router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('authToken') || sessionStorage.getItem('authToken'); 

    if (!token) {
      next({ name: 'Login' });
    }else{
      next();
    }
    /*else{
      const decodedToken = jwtDecode(token); // Puedes usar la librería jwt-decode
      const userRoles = decodedToken.sub || []; // Asumimos que el token tiene un campo 'roles'
      console.log(decodedToken);
      // Verificar si el usuario tiene uno de los roles permitidos para la ruta
      if (to.meta.roles && !to.meta.roles.some(sub => userRoles.includes(sub))) {
        // Si el usuario no tiene el rol adecuado, redirigir o mostrar un error
        next({ name: 'Login' }); // O redirigir a una página de acceso denegado
      } else {
        next(); // Si tiene el rol adecuado, permitir la navegación
      }
    } */
  } else {
    next();
  }
});


export default router