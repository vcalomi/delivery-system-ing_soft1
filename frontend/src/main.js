import { createApp } from 'vue'
import App from './App.vue'
import boostrap from 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css';


const  app = createApp(App)
app.use(boostrap).mount('#app')

