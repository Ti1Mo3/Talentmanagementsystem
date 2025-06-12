import { createRouter, createWebHistory } from 'vue-router';
import Datenmanager from '../views/Datenmanager.vue';

const routes = [
  {
    path: '/',
    name: 'Datenmanager',
    component: Datenmanager,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
