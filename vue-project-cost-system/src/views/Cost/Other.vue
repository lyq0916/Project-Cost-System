<template>
  <div>
      <div class="crumbs">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item>
            <i class="el-icon-lx-calendar"></i> 表单
          </el-breadcrumb-item>
          <el-breadcrumb-item>其他支出</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="container">
        <div class="handle-box">
          <div class="table-header">
            <el-form :model="form">
              <el-form-item>
                <el-row>
                  <div class="header-left">
                    <el-autocomplete
                        v-model="form1.pname"
                        :fetch-suggestions="querySearch"
                        popper-class="my-autocomplete"
                        placeholder="Please input"
                        @select="handleSelect1"
                    >
                      <template #suffix>
                        <el-icon class="icon-mya-Group46" @click="handleIconClick1">
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
                    <el-button type="primary" @click="dialogFormVisible = true" style="height: 30px ;margin: 10px;">新增
                    </el-button>
                  </div>
                </el-row>
              </el-form-item>
            </el-form>
            <el-table :data="tableData1" style="width: 100%" border>
              <el-table-column prop="id" label="项目编号"></el-table-column>
              <el-table-column prop="name" label="名称"></el-table-column>
              <el-table-column prop="note" label="备注"></el-table-column>
              <el-table-column prop="money" label="费用(万元)"></el-table-column>
              <el-table-column prop="project" label="所属项目"></el-table-column>
              <el-table-column prop="date" label="日期"></el-table-column>
            </el-table>
          </div>
        </div>
      </div>

    <el-dialog v-model="dialogFormVisible" title="其他支出记录">
      <OtherAdd @cancel="cancel"></OtherAdd>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {getCurrentInstance, inject, onMounted, reactive, ref, shallowReactive, toRaw} from "vue";
import OtherAdd from "./Dialog/OtherAdd.vue";

const currentInstance = getCurrentInstance();
const {$http} = currentInstance.appContext.config.globalProperties;

const reload=inject('reload')

interface LinkItem {
  value: string
  id: string
}

const state = ref('')
const pid = ref<LinkItem[]>([])

const querySearch = (queryString: string, cb) => {
  const results = queryString
      ? pid.value.filter(createFilter(queryString))
      : pid.value
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
  pid.value = loadAll()
})


//其他支出表格展示
const form1 = reactive({
  pname: '',
})

const tableData1 = shallowReactive([])
$http.get('/getOther').then(res => {
  for (let i = 0; i < res.data.length; i++) {
    tableData1.push({
      id:res.data[i].id,
      name:res.data[i].name,
      money:res.data[i].money,
      project:res.data[i].project,
      date:res.data[i].date,
    })
  }
})

const handleSelect1 = (item: LinkItem) => {
  //console.log(toRaw(item).id)
  tableData1.splice(0, tableData1.length);
  $http.get('/getOtherByPid?pid='+toRaw(item).id).then(res => {
    for (let i = 0; i < res.data.length; i++) {
      tableData1.push({
        id:res.data[i].id,
        name:res.data[i].name,
        money:res.data[i].money,
        project:res.data[i].project,
        date:res.data[i].date,
      })
    }
  })
}


function show1() {
  form1.pname = ""
  tableData1.splice(0, tableData1.length);
  $http.get('/getOther').then(res => {
    for (let i = 0; i < res.data.length; i++) {
      tableData1.push({
        id:res.data[i].id,
        name:res.data[i].name,
        money:res.data[i].money,
        project:res.data[i].project,
        date:res.data[i].date,
      })
    }
  })
}

const handleIconClick1 = (ev: Event) => {
  //console.log(ev)
}

//新增记录表单
let dialogFormVisible = ref(false)
const formLabelWidth = '140px'
const num = ref(1)
const form = reactive({
  name: '',
  // type: '',
  pid: '',
  note: '',
  date: '',
})

const handleSelect2 = (item: LinkItem) => {
  form.pid=toRaw(item).id
}

function cancel() {
  dialogFormVisible.value = false;
}


</script>

<style scoped>

</style>