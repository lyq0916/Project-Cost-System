<template>
  <el-card>
    <el-row>
      <div class="m">基本信息</div>
    </el-row>
    <el-divider/>
    <el-row>
      <div>项目名称：{{ p.pname }}</div>
    </el-row>
    <el-row>
      <div>项目总预算：{{ p.budget }} 万元</div>
    </el-row>
    <el-row>
      <div>项目类型：<el-tag>{{ p.type }}</el-tag></div>
    </el-row>
    <el-row>
      <div>地点：<icon class="icon-mya-Group27"></icon>{{ p.address }}</div>
    </el-row>
    <el-row>
      <div>负责人：{{ p.header }}</div>
    </el-row>
  </el-card>
</template>

<script lang="ts" setup>
import {getCurrentInstance, reactive} from "vue";

const props = defineProps({
  pid: Number,
})
const currentInstance = getCurrentInstance();
const {$http} = currentInstance.appContext.config.globalProperties;

const p = reactive({
  pname: "",
  budget: "",
  type: "",
  address: "",
  header: ""
})
$http.get('/getprojectbyid?pid=' + props.pid).then(res => {
  p.pname=res.data.pname
  p.budget=res.data.budget
  p.type=res.data.type
  p.address=res.data.address
  p.header=res.data.header
})
</script>

<style scoped>
.m {
  font-size: large;
}
.el-row {
  margin-bottom: 20px;
}
</style>