import Vue from 'vue'
import Router from 'vue-router'
// 导入刚才编写的组件
/*
import AppIndex from '@/components/home/AppIndex'
*/
import Login from '@/components/Login'
import Grade from '@/components/home/teachResault_grade'
import Check from '@/components/home/teachResault_check'
import Show from '@/components/home/teachResault_show'
import File from '@/components/home/teachResault_file'
import manage from '@/components/headTop'
import home from '@/components/home/home'

Vue.use(Router)

const originalPush = Router.prototype.push
Router.prototype.push = function push (location, onResolve, onReject) {
  if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
  return originalPush.call(this, location).catch(err => err)
}

export default new Router({
  routes: [
    // 下面都是固定的写法
    {
      path: '/',
      name: 'Login',
      component: Login
    },
    {
      path: '/manage',
      name: 'manage',
      component: manage,
      children: [{
        path: '',
        name: 'home',
        component: home,
        meta: {requireAuth: true}
      }, {
        path: '/index',
        name: 'teachResault_grade',
        component: Grade,
        meta: {requireAuth: true}
      }, {
        path: '/check',
        name: 'teachResault_check',
        component: Check,
        meta: {requireAuth: true}
      }, {
        path: '/show',
        name: 'teachResault_show',
        component: Show,
        meta: {requireAuth: true}
      }, {
        path: '/file',
        name: 'teachResault_file',
        component: File,
        meta: {requireAuth: true}
      }
      ]
    }
  ]
})
