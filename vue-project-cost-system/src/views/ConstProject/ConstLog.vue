<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-calendar"></i> 表单
        </el-breadcrumb-item>
        <el-breadcrumb-item>施工日志</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <div class="table-header">
          <el-form :model="form1">
            <el-form-item>
              <el-row>
                <div class="header-left">
                  <el-date-picker
                      v-model="form1.date"
                      type="date"
                      placeholder="Pick a Date"
                      format="YYYY-MM-DD"
                      value-format="YYYY-MM-DD"
                      style="width: 150px;height:30px;margin: 10px"
                  />
                  <el-input v-model="form1.pname" placeholder="输入项目名" suffix-icon="icon-mya-Group46"
                            style="width: 150px;height:30px;margin: 10px"></el-input>
                  <el-button type="primary" @click="search" style="height: 30px ;margin: 10px">搜索</el-button>
                </div>
                <div class="header-right">
                  <el-button type="primary" @click="dialogFormVisible = true" style="height: 30px ;margin: 10px;">新增
                  </el-button>
                </div>
              </el-row>
            </el-form-item>
          </el-form>
        </div>
        <el-table :data="tableData" border stripe style="width: 100%">
          <el-table-column prop="pid" label="项目编号"/>
          <el-table-column prop="pname" label="项目名"/>
          <el-table-column prop="note" label="日志详情"/>
          <el-table-column type="date" prop="date" label="记录日期"/>
        </el-table>
      </div>
    </div>
    <el-dialog v-model="dialogFormVisible" title="施工日志">
      <el-form :model="form">
        <el-form-item label="项目编号" prop="id" label-width=140px>
          <el-input v-model="form.pid"/>
        </el-form-item>
        <el-form-item label="备注" prop="note" label-width=140px>
          <el-input v-model="form.note" :rows="3" placeholder="Please input"></el-input>
        </el-form-item>
        <el-form-item label="记录日期" prop="date" label-width=140px>
          <el-date-picker
              type="date"
              placeholder="请选择日期"
              v-model="form.date"
              value-format="YYYY-MM-DD"
              format="YYYY-MM-DD"
              style="width: 250px;">
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
import {getCurrentInstance, inject, reactive, ref, shallowReactive} from "vue";
import {ElMessage} from "element-plus";

const currentInstance = getCurrentInstance();
const {$http} = currentInstance.appContext.config.globalProperties;
const reload = inject('reload')

const tableData = shallowReactive([]);
$http.get('/logshow').then(res => {
  for (let i = 0; i < res.data.length; i++) {
    tableData.push({
      pid: res.data[i].pid,
      pname: res.data[i].pname,
      note: res.data[i].note,
      date: res.data[i].date,
    })
  }
})
//搜索
const form1 = reactive({
  date: '',
  pname: '',
})

function search() {
  $http.get('/searchlog?pname=' + form1.pname + '&date=' + form1.date).then(res => {
    tableData.splice(0, tableData.length);
    for (let i = 0; i < res.data.length; i++) {
      tableData.push({
        pid: res.data[i].pid,
        pname: res.data[i].pname,
        note: res.data[i].note,
        date: res.data[i].date,
      })
    }
  })
}

//新增记录表单
let dialogFormVisible = ref(false)
const form = reactive({
  pid: '',
  note: '',
  date: '',
})

//取消
function cancel() {
  dialogFormVisible.value = false;
}

//提交
function submitDialog() {
  const date = new Date(Date.parse(form.date));
  $http.post('/logadd', {
    pid: form.pid,
    note: form.note,
    date: form.date,
  }).then(res => {
    if (res.data.code == 200) {
      dialogFormVisible.value = false;
      ElMessage.success("提交成功");
      reload();
    } else {
      ElMessage.error("提交失败");
    }
  })
}
</script>

<style scoped>
.table-header .header-right {
  position: absolute;
  right: 5px;
}

.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>