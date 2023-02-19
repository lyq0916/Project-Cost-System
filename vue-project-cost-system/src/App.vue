<template>
    <keep-alive>
      <router-view v-if="state.showRouter&& $route.meta.keepAlive"/>
    </keep-alive>
  <router-view v-if="state.showRouter&&!$route.meta.keepAlive"></router-view>
</template>

<script>
import * as echarts from 'echarts'
import {nextTick, provide, reactive} from 'vue'
export default {
  name:'App',
  setup() {
      provide('ec',echarts);
      const state=reactive({
        showRouter: true,
      })
    function reload(){
        state.showRouter=false;
        nextTick(() =>{
          state.showRouter=true
        })
    }
    provide('reload',reload);
      return{
        state
      }
    },
};
</script>

<style>
@import "./assets/css/main.css";
@import "./assets/css/color-dark.css";
</style>
