<template>
  <body id="poster">
  <el-form class="login-container" label-position="left"
           label-width="0px">
    <h3 class="login_title">系统登录</h3>
    <el-form-item  prop="username">
      <el-input type="text" v-model="param.username"
                auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>
    <el-form-item prop="password">
      <el-input type="password" v-model="param.password"
                auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>
    <el-form-item style="width: 100%">
      <el-button type="primary" style="float:left;width: 100%;background: #505458;border: none" v-on:click="submitForm">登录</el-button>
    </el-form-item>
    <el-form-item>
      <el-link style="margin-right: 3px">忘记密码</el-link>
      <el-link href="#/register" target="_blank" >| 注册账号</el-link>
    </el-form-item>
  </el-form>
  </body>

</template>

<script>
import {ref, reactive, getCurrentInstance} from "vue";
import {useStore} from "vuex";
import {useRouter} from "vue-router";
import {ElMessage} from "element-plus";

export default {
  setup() {
    const router = useRouter();
    const store = useStore();
    const param = reactive({
      username: "admin",
      password: "123",
      photo: ""
    });

    //输入规则
    const rules = {
      username: [
        {
          required: true,
          message: "请输入用户名",
          trigger: "blur",
        },
      ],
      password: [
        {required: true, message: "请输入密码", trigger: "blur"},
      ],
    };

    const currentInstance = getCurrentInstance()
    const {$http} = currentInstance.appContext.config.globalProperties

    function submitForm() {
      $http.post("/login", {
        username: param.username,
        password: param.password
      }).then(res => {
        if (res.data.code === 200) {
          ElMessage.success("登录成功");
          localStorage.setItem("username", param.username);
          //登录成功后把用户信息查出来放进store中
          //异步问题！！！信息放入store中后再跳转
          $http.post("/getusr", {
            username: param.username,
            password: param.password
          }).then(res => {
            store.commit('login', res.data);
            //判断用户是不是项目负责人 ---如果是就把该用户负责的项目放进store中(在main.js动态加载菜单中实现)
            $http.get("/isHeader?uid="+res.data.id).then(response =>{
              if(response.data==true){
                const headerbean={
                  header:response.data,
                  uid:res.data.id,
                };
                store.commit('isHeader', headerbean);
              }
            })
            router.push("/projectdetails")
          })
        } else {
          ElMessage.error("登录失败");
          return false;
        }
      })
    }
    store.commit("clearTags");

    return {
      param,
      rules,
      submitForm,
    };
  },
};
</script>

<style scoped>
.login-btn button {
  width: 100%;
  height: 36px;
  margin-bottom: 10px;
}

.login-container {
  border-radius: 15px;
  background-clip: padding-box;
  margin: 100px auto;
  width: 350px;
  padding: 35px 35px 15px 35px;
  background: rgba(255, 255, 255, 0.3);
}

.login_title {
  margin: 0px auto 40px auto;
  text-align: center;
  color: #f0f0f0;
}

#poster {
  background: url("src/assets/img/login-bg.jpg") no-repeat center;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
}

body {
  margin: 0px;
}


</style>