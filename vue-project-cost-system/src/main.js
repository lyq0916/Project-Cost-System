import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import installElementPlus from './plugins/element'
import './assets/css/icon.css'
import axios from 'axios'
import * as echarts from 'echarts'
import './assets/icon/iconfont.css'

//设置反向代理，前端请求默认发送到8443
axios.defaults.baseURL = "http://localhost:8443/api"
//让前段每次发送请求能带上cookie--sessionId shiro就可以通过sessionID获取登录状态并执行是否登录的判断
axios.defaults.withCredentials = true //使请求带上凭证信息

const app = createApp(App)
app.config.globalProperties.$http = axios
app.echarts = echarts

installElementPlus(app)
app
    .use(store)
    .use(router)
    .mount('#app')

const formatRoutes = (routes) => {
    let fmtRoutes = []
    routes.forEach(route => {
        if (route.children) {
            route.children = formatRoutes(route.children)
        }
        let formatRoute = {
            path: route.path,
            component:() => import("../views/"+route.component+'.vue'),
            name: route.namezh,
            icon: route.icon,
            children: route.children==null?[]:route.children
        }
        fmtRoutes.push(formatRoute)
    })
    return fmtRoutes
}

router.beforeEach((to, from, next) => {
    //用户已经登录，动态加载菜单
    if (store.state.user.username) {
        initMenu(router,store)
    }
    next()
})

const initMenu = (router, store) => {
    //菜单已经加载过
    if(store.state.menus.length > 0){
        return
    }
    //初始化
    else{
        axios.get('/menu').then(res => {
            if(res && res.status===200){
                const fmtRoutes = formatRoutes(res.data);
                store.commit('initMenu',fmtRoutes)
            }
        })
    }
}