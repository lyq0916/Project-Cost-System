import vue from '@vitejs/plugin-vue'
/**
 *　　┏┓　　　┏┓+ +
 *　┏┛┻━━━┛┻┓ + +
 *　┃　　　　　　　┃ 　
 *　┃　　　━　　　┃ ++ + + +
 * ████━████ ┃+
 *　┃　　　　　　　┃ +
 *　┃　　　┻　　　┃
 *　┃　　　　　　　┃ + +
 *　┗━┓　　　┏━┛
 *　　　┃　　　┃　　　　　　　　　　　
 *　　　┃　　　┃ + + + +
 *　　　┃　　　┃
 *　　　┃　　　┃ +  神兽保佑
 *　　　┃　　　┃    代码无bug　　
 *　　　┃　　　┃　　+　　　　　　　　　
 *　　　┃　 　　┗━━━┓ + +
 *　　　┃ 　　　　　　　┣┓
 *　　　┃ 　　　　　　　┏┛
 *　　　┗┓┓┏━┳┓┏┛ + + + +
 *　　　　┃┫┫　┃┫┫
 *　　　　┗┻┛　┗┻┛+ + + +
 */
/*
* vite默认端口号3000
*
* */
export default {
    server: {
        /* 自动打开浏览器 */
        open: true,
        /* 使用代理 */
        proxy: {
            '/api': {
                /* 目标代理服务器地址 */
                target: 'http://localhost:8443',
                /* 允许跨域 */
                changeOrigin: true,
                pathRewrite: {
                    "^/api": ""
                }
            },
        },
    },
    base: './',
    plugins: [vue()],
    optimizeDeps: {
        include: ['schart.js']
    },
    css: {
        preprocessorOptions: {
            scss: {
                additionalData: '@import "./src/assets/scss/global.scss";' // 添加公共样式
            }
        }
    },
}