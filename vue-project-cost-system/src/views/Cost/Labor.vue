<template>
  <div>
    <!--员工详情 -->
    <el-card>
      <div class="crumbs">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item>
            <i class="el-icon-lx-calendar"></i> 表单
          </el-breadcrumb-item>
          <el-breadcrumb-item>员工详情</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="container">
        <div class="handle-box">
          <div class="table-header">
            <el-form :model="form1">
              <el-form-item>
                <el-row>
                  <div class="header-left">
                    <el-autocomplete
                        v-model="form1.pname"
                        :fetch-suggestions="querySearch"
                        popper-class="my-autocomplete"
                        placeholder="Please input"
                        @select="handleSelect1"
                        style="height: 30px ;margin: 10px;"
                    >
                      <template #suffix>
                        <el-icon class="icon-mya-Group46">
                          <edit/>
                        </el-icon>
                      </template>
                      <template #default="{ item }">
                        <div class="value">{{ item.value }}</div>
                        <span class="link">项目编号:{{ item.id }}</span>
                      </template>
                    </el-autocomplete>
                    <el-button type="primary" @click="show1" style="height: 30px ;margin: 10px;">显示全部
                    </el-button>
                  </div>
                  <div class="header-right">
                    <el-button type="primary" @click="dialogFormVisible1 = true" style="height: 30px ;margin: 10px;">新增
                    </el-button>
                  </div>
                </el-row>
              </el-form-item>
            </el-form>
            <el-table :data="tableData1" border stripe style="width: 100%">
              <el-table-column label="员工编号">
                <template #default="scope">
                  <div style="display: flex; align-items: center">
                    <span style="margin-left: 10px">{{ scope.row.id }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="姓名">
                <template #default="scope">
                  <div style="display: flex; align-items: center">
                    <span style="margin-left: 10px">{{ scope.row.name }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="工种">
                <template #default="scope">
                  <div style="display: flex; align-items: center">
                    <span style="margin-left: 10px">{{ scope.row.type }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="所属项目">
                <template #default="scope">
                  <div style="display: flex; align-items: center">
                    <span style="margin-left: 10px">{{ scope.row.p }}</span>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 薪资支出-->
    <el-card>
      <div class="crumbs">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item>
            <i class="el-icon-lx-calendar"></i> 表单
          </el-breadcrumb-item>
          <el-breadcrumb-item>薪资支出</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="container">
        <div class="handle-box">
          <div class="table-header">
            <el-form :model="form2">
              <el-form-item>
                <el-row>
                  <div class="header-left">

                    <el-autocomplete
                        v-model="form2.pname"
                        :fetch-suggestions="querySearch"
                        popper-class="my-autocomplete"
                        placeholder="Please input"
                        @select="handleSelect2"
                        style="height: 30px ;margin: 10px;"
                    >
                      <template #suffix>
                        <el-icon class="icon-mya-Group46">
                          <edit/>
                        </el-icon>
                      </template>
                      <template #default="{ item }">
                        <div class="value">{{ item.value }}</div>
                        <span class="link">项目编号:{{ item.id }}</span>
                      </template>
                    </el-autocomplete>
                    <el-button type="primary" @click="show2" style="height: 30px ;margin: 10px;">显示全部
                    </el-button>
                  </div>
                  <div class="header-right">
                    <el-button type="primary" @click="dialogFormVisible2 = true" style="height: 30px ;margin: 10px;">新增
                    </el-button>
                  </div>
                </el-row>
              </el-form-item>
            </el-form>
            <el-table :data="tableData2" border stripe style="width: 100%">
              <el-table-column prop="id" label="记录编号"></el-table-column>
              <el-table-column prop="ename" label="员工名"></el-table-column>
              <el-table-column prop="basic" label="基本工资(元)"></el-table-column>
              <el-table-column prop="allowance" label="加班津贴(元)"></el-table-column>
              <el-table-column prop="push" label="提成(元)"></el-table-column>
              <el-table-column prop="sum" label="总工资(元)"></el-table-column>
              <el-table-column prop="date" label="日期"></el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </el-card>


    <el-dialog v-model="dialogFormVisible1" title="新增员工">
      <EmpAdd @cancel="cancel1"></EmpAdd>
    </el-dialog>

    <el-dialog v-model="dialogFormVisible2" title="薪资支出记录">
      <EmpPayAdd @cancel="cancel2"></EmpPayAdd>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {getCurrentInstance, inject, onMounted, reactive, ref, shallowReactive, toRaw} from "vue";
import {Edit} from '@element-plus/icons-vue'
import EmpPayAdd from "./Dialog/EmpPayAdd.vue";
import EmpAdd from "./Dialog/EmpAdd.vue";

const currentInstance = getCurrentInstance();
const {$http} = currentInstance.appContext.config.globalProperties;
const reload = inject('reload')

let dialogFormVisible1 = ref(false)
let dialogFormVisible2 = ref(false)


interface LinkItem {
  value: string
  id: string
}

const state = ref('')
const project = ref<LinkItem[]>([])

const querySearch = (queryString: string, cb) => {
  const results = queryString
      ? project.value.filter(createFilter(queryString))
      : project.value
  // call callback function to return suggestion objects
  cb(results)
}
const createFilter = (queryString) => {
  return (restaurant) => {
    return (
        restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
    )
  }
}
const loadAll = () => {
  let p = []
  $http.get('/projectshow').then(res => {
    for (let i = 0; i < res.data.length; i++) {
      p.push({value: res.data[i].pname, id: res.data[i].pid})
    }
  })
  return p
}

onMounted(() => {
  project.value = loadAll()
})


//员工
const form1 = reactive({
  pname: '',
})

const tableData1 = shallowReactive([])

$http.get('/getEmp').then(res => {
  for (let i = 0; i < res.data.length; i++) {
    tableData1.push({id: res.data[i].id, name: res.data[i].name, type: res.data[i].type, p: res.data[i].pid})
  }
})

const handleSelect1 = (item: LinkItem) => {
  //console.log(toRaw(item).id)
  tableData1.splice(0, tableData1.length);
  $http.get('/getEmpByPid?pid=' + toRaw(item).id).then(res => {
    for (let i = 0; i < res.data.length; i++) {
      tableData1.push({id: res.data[i].id, name: res.data[i].name, type: res.data[i].type, p: res.data[i].pid})
    }
  })
}

function show1() {
  form1.pname = ""
  tableData1.splice(0, tableData1.length);
  $http.get('/getEmp').then(res => {
    for (let i = 0; i < res.data.length; i++) {
      tableData1.push({id: res.data[i].id, name: res.data[i].name, type: res.data[i].type, p: res.data[i].pid})
    }
  })
}

const cancel1 = (val) => {
  dialogFormVisible1.value = false;
}


//薪资结算
const form2 = reactive({
  pname: '',
})

const tableData2 = shallowReactive([])
$http.get('/getEmpPay').then(res => {
  for (let i = 0; i < res.data.length; i++) {
    tableData2.push({
      id: res.data[i].id, ename: res.data[i].eid, basic: res.data[i].basic,
      allowance: res.data[i].allowance, push: res.data[i].push, sum: res.data[i].money * 10000, date: res.data[i].date
    })
  }
})

const handleSelect2 = (item: LinkItem) => {
  tableData2.splice(0, tableData2.length);
  $http.get('/getEmpPayByPid?pid=' + toRaw(item).id).then(res => {
    for (let i = 0; i < res.data.length; i++) {
      tableData2.push({
        id: res.data[i].id, ename: res.data[i].eid, basic: res.data[i].basic,
        allowance: res.data[i].allowance, push: res.data[i].push, sum: res.data[i].money * 10000, date: res.data[i].date
      })
    }
  })
}

function show2() {
  form2.pname = ""
  tableData2.splice(0, tableData2.length);
  $http.get('/getEmpPay').then(res => {
    for (let i = 0; i < res.data.length; i++) {
      tableData2.push({
        id: res.data[i].id, ename: res.data[i].eid, basic: res.data[i].basic,
        allowance: res.data[i].allowance, push: res.data[i].push, sum: res.data[i].money * 10000, date: res.data[i].date
      })
    }
  })
}

const cancel2 = (val) => {
  dialogFormVisible2.value = false;
}

</script>

<style scoped>
.table-header .header-right {
  position: absolute;
  right: 5px;
}
</style>