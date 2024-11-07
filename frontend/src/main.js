import { createApp } from 'vue'
import App from './App.vue'
import bootstrap from 'bootstrap'
import router from './router.js'
import 'bootstrap/dist/css/bootstrap.min.css';


const  app = createApp(App)
app.use(router)
app.use(bootstrap)
app.mount('#app')

