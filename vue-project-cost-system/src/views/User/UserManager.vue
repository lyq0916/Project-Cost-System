<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-calendar"></i> 表单
        </el-breadcrumb-item>
        <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <div class="table-header">
          <el-form :model="form">
            <el-form-item>
              <el-row>
                <el-input v-model="form.name" placeholder="输入用户名" suffix-icon="icon-mya-Group46"
                          style="width: 150px;height:30px;margin: 10px"></el-input>
                <el-button type="primary" @click="search" style="height: 30px ;margin: 10px">搜索</el-button>
              </el-row>
            </el-form-item>
          </el-form>
        </div>
        <el-table :data="tableData" border stripe style="width: 100%">
          <el-table-column prop="id" label="用户ID"/>
          <el-table-column prop="name" label="用户名"/>
          <el-table-column prop="oprations" label="操作">
            <template #default="scope">
              <el-button type="primary" size="small" @click="edit(scope.$index,scope.row)" plain>编辑</el-button>
              <el-button type="danger" size="small" @click="deleteuser(scope.$index, scope.row)" plain>删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <el-dialog v-model="dialogFormVisible" title="更改用户信息">
      <el-form :model="form1">
        <el-form-item label="名字">
          <el-input v-model="form1.name"></el-input>
        </el-form-item>
        <el-form-item label="用户角色">
          <el-checkbox-group v-model="checkList">
            <el-checkbox label="1">管理员</el-checkbox>
            <el-checkbox label="2">项目负责人</el-checkbox>
            <el-checkbox label="3">老板</el-checkbox>
            <el-checkbox label="4">普通职员</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancel">Cancel</el-button>
        <el-button type="primary" @click="submitDialog">Confirm</el-button>
      </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import {getCurrentInstance, inject, reactive, ref, shallowReactive, toRaw} from 'vue'
import {ElMessage} from "element-plus";

const reload=inject('reload')

const currentInstance = getCurrentInstance();
const {$http} = currentInstance.appContext.config.globalProperties;

const form = reactive({
  name: ''
})
const form1 = reactive({
  name: '',
})
const tableData = shallowReactive([])

let dialogFormVisible = ref(false)
const checkList = ref([])

interface User{
  id:String,
  name:String,
}

$http.get('/getUsers').then(res => {
  for (let i = 0; i < res.data.length; i++) {
    tableData.push({id: res.data[i].id, name: res.data[i].username})
  }
})

function search() {
  $http.get('/search?name=' + form.name).then(res => {
    tableData.splice(0, tableData.length);
    for (let i = 0; i < res.data.length; i++) {
      tableData.push({id: res.data[i].id, name: res.data[i].username})
    }
    reload()
  })
}

let id;
let name;
function edit(index: number, row: User) {
  dialogFormVisible.value = true
  id=row.id;
  name=row.name;
  $http.get('/getRolesById?id='+row.id).then(res => {
    checkList.value.splice(0,checkList.value.length)
    for(let i=0;i<res.data.length;i++) {
      checkList.value.push(res.data[i].toString());
    }
  })
}

function deleteuser(index: number, row: User) {
  $http.get('/deleteUser?id='+row.id).then(res => {
    if(res.data.code == 200){
      ElMessage.success("删除成功")
      reload()
    }else{
      ElMessage.error("删除失败")
    }
  })
}

function cancel(){
  dialogFormVisible.value=false
}

function submitDialog(){
  console.log(toRaw(checkList.value))
  $http.post('/updateUser',{
    id:id,
    username:name,
    roles:toRaw(checkList.value)
  }).then(res => {
    if(res.data.code == 200){
      ElMessage.success("修改成功")
      dialogFormVisible.value=false
    }else{
      ElMessage.error("修改失败")
    }
  })
}

</script>

<style scoped>

</style>