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
      <div>项目类型：
        <el-tag>{{ p.type }}</el-tag>
      </div>
    </el-row>
    <el-row>
      <div>地点：
        <icon class="icon-mya-Group27"></icon>
        {{ p.address }}
      </div>
    </el-row>
    <el-row>
      <div>负责人：{{ p.header }}</div>
    </el-row>
    <el-row>
      <div>预计开工：<icon class="icon-mya-Group13"></icon>{{ p.estart }}</div>
    </el-row>
    <el-row>
      <div>预计完工：<icon class="icon-mya-Group13"></icon>{{ p.eend }}</div>
    </el-row>
    <el-row>
      <div v-if="p.s">开工日期：<icon class="icon-mya-Group13"></icon>{{ p.start }}</div>
    </el-row>
    <el-row>
      <div v-if="p.e">完工日期：<icon class="icon-mya-Group13"></icon>{{ p.end }}</div>
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
  header: "",
  estart: "",
  eend: "",
  start: "",
  end: "",
  s: false,
  e: false,
})
$http.get('/getprojectbyid?pid=' + props.pid).then(res => {
  p.pname = res.data.pname
  p.budget = res.data.budget
  p.type = res.data.type
  p.address = res.data.address
  p.header = res.data.header
})
$http.get('/findConstByPid?pid=' + props.pid).then(res => {
  p.estart=res.data.estart
  p.eend=res.data.eend
  if(res.data.start!=null){
    p.s=true
    p.start=res.data.start
  }
  if(res.data.end!=null){
    p.e=true
    p.end=res.data.end
  }
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