import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Vidar',
      component: () => import('@/views/Vidar/AppIndex.vue')
    },
  ]
});

router.beforeEach((to, from, next) => {
  next();
});

export default router;