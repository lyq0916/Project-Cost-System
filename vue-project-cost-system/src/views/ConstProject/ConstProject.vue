<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-calendar"></i> 表单
        </el-breadcrumb-item>
        <el-breadcrumb-item>施工管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <div class="table-header">
          <el-table :data="tableData" style="width: 100%">
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button type="success" size="small" v-if="scope.row.state=='准备中'"
                           @click="handleStart(scope.$index, scope.row)" plain>正式开工
                </el-button>
                <el-button type="primary" size="small" v-if="scope.row.state=='开工中'"
                           @click="handleEnd(scope.$index, scope.row)" plain>设为完工
                </el-button>
                <el-button type="info" size="small" v-if="scope.row.state=='已完工'" plain>无操作</el-button>
              </template>
            </el-table-column>
            <el-table-column label="项目编号" width="150">
              <template #default="scope">
                <div style="display: flex; align-items: center">
                  <span style="margin-left: 10px">{{ scope.row.pid }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="项目名称" width="150">
              <template #default="scope">
                <el-popover effect="light" trigger="hover" placement="top" width="auto">
                  <template #default>
                    <div>项目编号: {{ scope.row.pid }}</div>
                    <div>项目名称: {{ scope.row.pname }}</div>
                  </template>
                  <template #reference>
                    <span style="margin-left: 10px">{{ scope.row.pname }}</span>
                  </template>
                </el-popover>
              </template>
            </el-table-column>
            <el-table-column label="项目状态" width="150">
              <template #default="scope">
                <div style="display: flex; align-items: center">
                  <span style="margin-left: 10px">{{ scope.row.state }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="项目详情" width="150">
              <template #default="scope">
                <el-button size="small" @click="handleShow(scope.$index, scope.row)"
                >查看
                </el-button>
              </template>
            </el-table-column>
            <el-table-column label="开工日期(预计)" width="150">
              <template #default="scope">
                <el-icon>
                  <timer/>
                </el-icon>
                <span style="margin-left: 10px">{{ scope.row.date1 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="开工日期(实际)" width="150">
              <template #default="scope">
                <el-icon>
                  <timer/>
                </el-icon>
                <span style="margin-left: 10px">{{ scope.row.date2 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="完工日期(预计)" width="150">
              <template #default="scope">
                <el-icon>
                  <timer/>
                </el-icon>
                <span style="margin-left: 10px">{{ scope.row.date3 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="完工日期(实际)" width="150">
              <template #default="scope">
                <el-icon>
                  <timer/>
                </el-icon>
                <span style="margin-left: 10px">{{ scope.row.date4 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="预算(万元)" width="150">
              <template #default="scope">
                <span style="margin-left: 10px">{{ scope.row.budget }}</span>
              </template>
            </el-table-column>
            <el-table-column label="已花成本" width="150">
              <template #default="scope">
                <span style="margin-left: 10px">{{ scope.row.cost }}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <el-dialog v-model="visible" title="项目详情">
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
    <el-dialog v-model="visible1" title="填写正式开工日期">
      <el-date-picker
          type="date"
          placeholder="请选择日期"
          v-model="start"
          value-format="YYYY-MM-DD"
          format="YYYY-MM-DD"
          style="width: 150px;margin-right: 10px"
      >
      </el-date-picker>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancel1">Cancel</el-button>
        <el-button type="primary" @click="submitDialog1">Confirm</el-button>
      </span>
      </template>
    </el-dialog>
    <el-dialog v-model="visible2" title="填写正式完工日期">
      <el-date-picker
          type="date"
          placeholder="请选择日期"
          v-model="end"
          value-format="YYYY-MM-DD"
          format="YYYY-MM-DD"
          style="width: 150px;margin-right: 10px"
      >
      </el-date-picker>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancel2">Cancel</el-button>
        <el-button type="primary" @click="submitDialog2">Confirm</el-button>
      </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import {Timer} from "@element-plus/icons-vue";
import {getCurrentInstance, inject, ref, shallowReactive} from "vue";
import {ElMessage} from "element-plus";

const currentInstance = getCurrentInstance();
const {$http} = currentInstance.appContext.config.globalProperties;
const reload = inject('reload');

let visible = ref(false);
let visible1 = ref(false);
let visible2 = ref(false);
const tableData = shallowReactive([]);
const tableData1 = shallowReactive([]);
const start = ref(null);
const end = ref(null);

interface Project {
  pid: String,
  pname: String,
  state: String,
  date1: String,
  date2: String,
  date3: String,
  date4: String,
  budget: String,
  cost: String,
}

const handleShow = (index: number, row: Project) => {
  //console.log(row.pid);
  visible.value = true;
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

/*正式开工---填写开工日期*/
let pid;
const handleStart = (index: number, row: Project) => {
  visible1.value = true;
  pid = row.pid;
  console.log(pid);
}

function cancel1() {
  visible1.value = false;
}

function submitDialog1() {
  $http.get('/start?pid=' + pid + '&start=' + start.value).then(response => {
    if (response.data.code == 200) {
      ElMessage.success("修改成功");
      visible1.value = false;
      reload();
    } else {
      ElMessage.error("修改失败");
    }
  })
}

const handleEnd = (index: number, row: Project) => {
  visible2.value = true;
  pid = row.pid;
}

function cancel2() {
  visible2.value = false;
}

function submitDialog2() {
  //设为已完工 --
  // 1.填入const_project完工日期
  //2.const_project状态设为已完工
  //3.project状态设为已完工
  $http.get('/end?pid=' + pid + '&end=' + end.value).then(response => {
    if (response.data.code == 200) {
      ElMessage.success("修改成功");
      visible2.value = false;
      reload();
    } else {
      ElMessage.error("修改失败");
    }
  })
}

$http.get('/getconstbeans').then(res => {
  for (let i = 0; i < res.data.length; i++) {
    tableData.push({
      pid: res.data[i].pid,
      pname: res.data[i].pname,
      state: res.data[i].state,
      date1: res.data[i].estart,
      date2: res.data[i].start,
      date3: res.data[i].eend,
      date4: res.data[i].end,
      budget: res.data[i].budget,
      cost: res.data[i].cost,
    })
  }
})

</script>

<style scoped>

</style>