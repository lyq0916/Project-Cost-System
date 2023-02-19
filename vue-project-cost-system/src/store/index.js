import {createStore} from 'vuex'
import axios from "axios";

export default createStore({
    state: {
        tagsList: [],
        collapse: false,
        username: window.sessionStorage.getItem('user' || '[]') == null ? '' : JSON.parse(window.sessionStorage.getItem('user' || '[]')).username,
        user: window.sessionStorage.getItem('user' || '[]') == null ? '' : JSON.parse(window.sessionStorage.getItem('user' || '[]')),
        menus: [],
        header:false,
        project:[]
    },
    mutations: {
        //登录时将user信息存入
        login(state, user) {
            state.user = user
            window.sessionStorage.setItem('user', JSON.stringify(user))
        },
        //判断是不是项目负责人
        isHeader(state, headerbean) {
            state.header = headerbean;
            //如果是项目负责人
            if(headerbean.header){
                axios.get('/getprojectbyuid?uid='+headerbean.uid).then(res =>{
                    state.project=res.data;
                })
            }
        },
        delTagsItem(state, data) {
            state
                .tagsList
                .splice(data.index, 1);
        },
        setTagsItem(state, data) {
            state
                .tagsList
                .push(data)
        },
        clearTags(state) {
            state.tagsList = []
        },
        closeTagsOther(state, data) {
            state.tagsList = data;
        },
        closeCurrentTag(state, data) {
            for (let i = 0, len = state.tagsList.length; i < len; i++) {
                const item = state.tagsList[i];
                if (item.path === data.$route.fullPath) {
                    if (i < len - 1) {
                        data
                            .$router
                            .push(state.tagsList[i + 1].path);
                    } else if (i > 0) {
                        data
                            .$router
                            .push(state.tagsList[i - 1].path);
                    } else {
                        data
                            .$router
                            .push("/");
                    }
                    state
                        .tagsList
                        .splice(i, 1);
                    break;
                }
            }
        },
        // 侧边栏折叠
        handleCollapse(state, data) {
            state.collapse = data;
        },
        initMenu(state, menus) {
            state.menus = menus
        }
    },
    actions: {},
    modules: {}
})