import {createRouter, createWebHistory} from 'vue-router'
import Library from '../views/Library'

const routes = [
    {
        path: '/',
        name: 'Library',
        component: Library
    },
    {
        path: '/books',
        name: 'Books',
        component: () => import('../views/Books.vue')
    },
    {
        path: '/comments?bookId=:bookId',
        name: 'Comments',
        component: () => import('../views/Comments.vue'),
        props: true
    },
    {
        path: '/authors',
        name: 'Authors',
        component: () => import('../views/Authors.vue')
    },
    {
        path: '/genres',
        name: 'Genres',
        component: () => import('../views/Genres.vue')
    }
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

export default router
