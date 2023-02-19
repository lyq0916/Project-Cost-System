<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-calendar"></i> 表单
        </el-breadcrumb-item>
        <el-breadcrumb-item>成本维护</el-breadcrumb-item>
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
          <el-table-column prop="id" label="记录编号"/>
          <el-table-column prop="name" label="名称"/>
          <el-table-column prop="type" label="支出类型"/>
          <el-table-column prop="money" label="支出金额(万元)"/>
          <el-table-column prop="pid" label="项目编号"/>
          <el-table-column type="date" prop="date" label="记录日期"/>
        </el-table>
      </div>
    </div>
    <!-- 新增成本维护记录 弹出表单对话框-->
    <el-dialog v-model="dialogFormVisible" title="成本支出记录">
      <el-form :model="form">
        <el-form-item label="记录名称" :label-width="formLabelWidth" prop="name">
          <el-input v-model="form.name" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="支出类型" :label-width="formLabelWidth" prop="type">
          <el-select v-model="form.type" placeholder="Please select type">
            <el-option label="材料费用" value="材料"/>
            <el-option label="机械设备" value="机械设备"/>
            <el-option label="人工费用" value="人工"/>
            <el-option label="间接费用" value="间接支出"/>
            <el-option label="其他" value="其他"/>
          </el-select>
        </el-form-item>
        <el-form-item label="支出金额" :label-width="formLabelWidth" prop="money">
          <el-input-number v-model="num" :precision="2" :step="0.1"/>
          <span style="margin-left: 10px">万元</span>
        </el-form-item>
        <el-form-item label="项目编号" :label-width="formLabelWidth" prop="id">
          <el-input v-model="form.pid"/>
        </el-form-item>
        <el-form-item label="备注" :label-width="formLabelWidth" prop="note">
          <el-input v-model="form.note" :rows="3" placeholder="Please input"></el-input>
        </el-form-item>
        <el-form-item label="支出日期" :label-width="formLabelWidth" prop="date">
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

<script setup>
import {ref, reactive, getCurrentInstance, shallowReactive, inject} from 'vue'
import {ElMessage} from "element-plus";

const currentInstance = getCurrentInstance();
const {$http} = currentInstance.appContext.config.globalProperties;

const reload=inject('reload')

//新增记录 表单数据
let dialogFormVisible = ref(false)
const formLabelWidth = '140px'
const num = ref(1)

const tableData = shallowReactive([]);

$http.get('/costshow').then(res => {
  for (let i = 0; i < res.data.length; i++) {
    tableData.push({
      id: res.data[i].mid,
      name: res.data[i].name,
      type: res.data[i].type,
      money: res.data[i].money,
      pid: res.data[i].pid,
      date: res.data[i].updatedate
    })
  }
})

const form = reactive({
  name: '',
  type: '',
  pid: '',
  date: '',
})

const form1=reactive({
  date:'',
  pname:'',
})

function search() {
  $http.get('/searchcost?pname='+form1.pname+'&date='+form1.date,{}).then(res => {
    tableData.splice(0, tableData.length);
    for (let i = 0; i < res.data.length; i++) {
      tableData.push({
        id: res.data[i].mid,
        name: res.data[i].name,
        type: res.data[i].type,
        money: res.data[i].money,
        pid: res.data[i].pid,
        date: res.data[i].updatedate
      })
    }
  })
}

function cancel() {
  dialogFormVisible.value=false;
}
function submitDialog() {
  const date = new Date(Date.parse(form.date));
  $http.post('/costadd', {
    name: form.name,
    type: form.type,
    money: num.value,
    pid: form.pid,
    updatedate: date,
  }).then((res => {
    if (res.data.code == 200) {
      dialogFormVisible.value = false;
      ElMessage.success("提交成功");
      reload();
    } else {
      ElMessage.error("提交失败");
    }
  }))
}

</script>

<style>
.table-header .header-right {
  position: absolute;
  right: 5px;
}

.dialog-footer button:first-child {
  margin-right: 10px;
}

</style>
