<template>
  <div class="container">
<!--    <el-row>
      <el-col>
        &lt;!&ndash;根据当前路由渲染chart！！！ 添加:key="$route.fullPath"&ndash;&gt;
          <costpie1 :key="$route.fullPath" :pid="user.id"></costpie1>
          <linechart :key="$route.fullPath" :pid="user.id"></linechart>

      </el-col>
    </el-row>-->
    <ConstDialog :key="$route.fullPath" :pid=user.id></ConstDialog>
  </div>
</template>

<script setup lang="ts">
import {onBeforeRouteUpdate, useRoute} from "vue-router"
import ConstDialog from "../Dialog/ConstDialog.vue";

const router = useRoute();
const user={
  id:router.query.name,
}
onBeforeRouteUpdate((to)=>{
  user.id=to.query.name;
})
</script>

<style scoped>
</style>