<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-calendar"></i> 表单
        </el-breadcrumb-item>
        <el-breadcrumb-item>投标详情</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <div class="table-header">
          <el-table :data="tableData" style="width: 100%">
            <el-table-column label="操作">
              <template #default="scope">
                <el-button type="primary" size="small" v-if="scope.row.state=='已报备'"
                           @click="handleWin(scope.$index, scope.row)" plain>已中标
                </el-button>
                <el-button type="danger" size="small" v-if="scope.row.state=='已报备'"
                           @click="handleFail(scope.$index, scope.row)" plain>已流标
                </el-button>
                <el-button type="success" size="small" v-if="scope.row.state=='已中标'"
                           @click="handleStart(scope.$index,scope.row)" plain>开工
                </el-button>
                <el-button type="info" size="small" v-if="scope.row.state=='建设中'" plain>无操作</el-button>
              </template>
            </el-table-column>
            <el-table-column label="项目编号">
              <template #default="scope">
                <div style="display: flex; align-items: center">
                  <span style="margin-left: 10px">{{ scope.row.pid }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="项目名称">
              <template #default="scope">
                <span style="margin-left: 10px">{{ scope.row.pname }}</span>
              </template>
            </el-table-column>
            <el-table-column label="项目状态">
              <template #default="scope">
                <div style="display: flex; align-items: center">
                  <span style="margin-left: 10px">{{ scope.row.state }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="项目详情">
              <template #default="scope">
                <el-button size="small" @click="handleShow(scope.$index, scope.row)"
                >查看
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <el-dialog v-model="visible1" title="项目详情">
      <el-table :data="tableData1" border stripe style="width: 100%">
        <el-table-column prop="id" label="项目编号"/>
        <el-table-column prop="name" label="项目名称"/>
        <el-table-column prop="bnumber" label="招标编号"/>
        <el-table-column prop="state" label="状态"/>
        <el-table-column prop="address" label="地址"/>
        <el-table-column prop="type" label="类型"/>
        <el-table-column prop="budget" label="预算(万元)"/>
        <el-table-column prop="date" label="报备日期"/>
      </el-table>
    </el-dialog>
    <el-dialog v-model="visible2" title="填写计划日期">
      <el-form :model="form">
        <el-form-item label="预计日期">
          <el-date-picker
              type="date"
              placeholder="预计开工日期"
              v-model="form.date1"
              value-format="YYYY-MM-DD"
              format="YYYY-MM-DD"
              style="width: 150px;margin-right: 10px"
          >
          </el-date-picker>
          <el-date-picker
              type="date"
              placeholder="预计完工日期"
              v-model="form.date2"
              value-format="YYYY-MM-DD"
              format="YYYY-MM-DD"
              style="width: 150px"
          >
          </el-date-picker>
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
import {getCurrentInstance, shallowReactive, ref, reactive, inject} from 'vue'
import {ElMessage} from "element-plus";

let visible1 = ref(false);
let visible2 = ref(false);
const form = reactive({
      date1: '',
      date2: '',
    }
);
const reload = inject('reload');
const tableData = shallowReactive([]);
const tableData1 = shallowReactive([]);

const value = ref(null);
console.log(value);

const currentInstance = getCurrentInstance();
const {$http} = currentInstance.appContext.config.globalProperties;

interface Project {
  pid: String,
  pname: String,
  state: String,
}

const handleShow = (index: number, row: Project) => {
  //console.log(row.pid);
  visible1.value = true;
  $http.get('/getprojectbyid?pid=' + row.pid, {}).then(response => {
    tableData1.splice(0,tableData1.length)
    tableData1.push({
      id: response.data.pid,
      name: response.data.pname,
      bnumber: response.data.bnumber,
      state: response.data.state,
      address: response.data.address,
      type: response.data.type,
      budget: response.data.budget,
      date: response.data.date,
    })
  })
}
const handleWin = (index: number, row: Project) => {
  //console.log(row.pid);
  $http.get('/updatebp?pid=' + row.pid + '&state=已中标').then(response => {
    if (response.data.code == 200) {
      ElMessage.success("修改成功");
      reload();
    } else {
      ElMessage.error("修改失败");
    }
  })
}

const handleFail = (index: number, row: Project) => {
  $http.get('/updatebp?pid=' + row.pid + '&state=已流标').then(response => {
    if (response.data.code == 200) {
      ElMessage.success("修改成功");
    } else {
      ElMessage.error("修改失败");
    }
  })
}

let pid;
const handleStart = (index: number, row: Project) => {
  visible2.value = true;
  pid = row.pid;
  console.log(pid);
}

function cancel() {
  visible2.value = false;
}

function submitDialog() {
  //修改项目为建设中 --
  // 1.向const_project中插入记录(准备中 有预计开工完工日期)
  // 2.修改bid_project中状态为建设中
  // 3.project中修改项目状态为建设中(在后端已经实现)
  $http.get('/addconst?pid=' + pid + '&start=' + form.date1 + '&end=' + form.date2).then(response => {
    if (response.data.code == 200) {
      $http.get('/updatebp?pid=' + pid + '&state=建设中').then(response => {
        if (response.data.code == 200) {
          ElMessage.success("修改成功");
          visible2.value = false;
          reload();
        } else {
          ElMessage.error("修改失败");
        }
      })
    } else {
      ElMessage.error("修改失败");
    }
  })

}

$http.get('/getbidbeans').then(res => {
  //console.log(res.data)
  for (let i = 0; i < res.data.length; i++) {
    tableData.push({
      pid: res.data[i].pid, pname: res.data[i].pname, state: res.data[i].state
    })
  }
})
</script>

<style scoped>

</style>