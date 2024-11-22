import { createRouter, createWebHistory } from 'vue-router'
import forgotPasswordComponent from './components/forgotPasswordComponent.vue'
import Register from './components/Register.vue'
import LoginComponent from './components/LoginComponent.vue'
import CreateProductComponent from './components/admin/CreateProductComponent.vue'
import CreateOrderComponent from './components/user/CreateOrderComponent.vue'
import ChangePasswordComponent from './components/ChangePasswordComponent.vue'
import OrdersAdminComponent from './components/admin/OrdersAdmin.vue'
import UploadPicture from './components/UploadPicture.vue'
import HomeView from './views/homeView.vue'
import { jwtDecode } from 'jwt-decode'

const routes = [
  {
    path: '/',
    name: 'HomeView',
    component: HomeView,
    meta: { 
      requiresAuth: true,
      role: [ 'ADMIN', 'USER']
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
    meta: { requiresAuth: true, role: ['ADMIN']  }
  },
  {
    path: '/createOrder',
    name: 'CreateOrderComponent',
    component: CreateOrderComponent,
    meta: { requiresAuth: true, role: ['USER'] }
  },
  {
    path: '/changePassword',
    name: 'ChangePasswordComponent',
    component: ChangePasswordComponent,
    meta: { requiresAuth: true, role: ['ADMIN', 'USER'] }
  },
  {
    path: '/ordersAdmin',
    name: 'ordersAdmin',
    component: OrdersAdminComponent,
    meta: { requiresAuth: true, role: ['ADMIN'] }
  },
  {
    path: '/uploadPicture',
    name: 'UploadPicture',
    component: UploadPicture,
    meta: { requiresAuth: true, role: ['ADMIN', 'USER'] }
  }
]


const router = createRouter({
  history: createWebHistory(),
  routes
})



router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('authToken') || sessionStorage.getItem('authToken'); 

    if (!token) {
      next({ name: 'Login' });
    }else{
      const decodedToken = jwtDecode(token);
      const userRoles = decodedToken.role || []; // el token tiene un campo 'role'
      // Verificar si el usuario tiene uno de los roles permitidos para la ruta
      if (to.meta.roles && !to.meta.roles.some(role => userRoles.includes(role))) {
        // Si el usuario no tiene el rol adecuado, redirigir o mostrar un error
        next({ name: 'Login' }); // O redirigir a una página de acceso denegado
      } else {
        next(); // Si tiene el rol adecuado, permitir la navegación
      }
    } 
  } else {
    next();
  }
});


export default router