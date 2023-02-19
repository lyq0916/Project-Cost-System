<template>
  <body id="poster">
  <el-form class="login-container" label-position="left"
           label-width="0px">
    <h3 class="login_title">注册</h3>
    <el-form-item  prop="username">
      <el-input type="text" v-model="param.username"
                auto-complete="off" placeholder="username"></el-input>
    </el-form-item>
    <el-form-item prop="password">
      <el-input type="password" v-model="param.password"
                auto-complete="off" placeholder="password"></el-input>
    </el-form-item>
    <el-form-item style="width: 100%">
      <el-button type="primary" style="float:left;width: 100%;background: #505458;border: none" v-on:click="submitForm">提交</el-button>
    </el-form-item>
    <el-form-item>
      <el-link href="#/login" target="_blank" >返回登录</el-link>
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
      username: "",
      password: "",
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
      $http.post('/register',{
        username: param.username,
        password: param.password
      }).then(response => {
        console.log(response)
        if(response.data.code==200){
          ElMessage.success("注册成功")
        }else{
          ElMessage.error("用户名已被占用！")
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