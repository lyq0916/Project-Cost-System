<template>
  <div>
    <el-card>
      <div class="crumbs">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item>
            <i class="el-icon-lx-calendar"></i> 表单
          </el-breadcrumb-item>
          <el-breadcrumb-item>间接支出</el-breadcrumb-item>
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
                        v-model="form.pname"
                        :fetch-suggestions="querySearch"
                        popper-class="my-autocomplete"
                        placeholder="Please input"
                        @select="handleSelect"
                    >
                      <template #suffix>
                        <el-icon class="icon-mya-Group46" @click="handleIconClick">
                          <edit/>
                        </el-icon>
                      </template>
                      <template #default="{ item }">
                        <div class="value">{{ item.value }}</div>
                        <span class="link">项目编号:{{ item.link }}</span>
                      </template>
                    </el-autocomplete>

                    <el-button type="primary" @click="show" style="height: 30px ;margin: 10px;">显示全部
                    </el-button>
                  </div>
                  <div class="header-right">
                    <el-button type="primary" @click="dialogFormVisible = true" style="height: 30px ;margin: 10px;">新增
                    </el-button>
                  </div>
                </el-row>
              </el-form-item>
            </el-form>
            <el-table :data="tableData" style="width: 100%" border>
              <el-table-column prop="id" label="记录编号"></el-table-column>
              <el-table-column prop="type" label="类型"></el-table-column>
              <el-table-column prop="note" label="备注"></el-table-column>
              <el-table-column prop="money" label="费用(万元）"></el-table-column>
              <el-table-column prop="project" label="所属项目"></el-table-column>
              <el-table-column prop="date" label="日期"></el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </el-card>
    <el-dialog v-model="dialogFormVisible" title="新增机械设备">
      <IndirectAdd @cancel="cancel"></IndirectAdd>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import {getCurrentInstance, inject, onMounted, reactive, ref, shallowReactive, toRaw} from 'vue'
import {Edit} from '@element-plus/icons-vue'
import IndirectAdd from "./Dialog/IndirectAdd.vue";

const currentInstance = getCurrentInstance();
const {$http} = currentInstance.appContext.config.globalProperties;
const reload = inject('reload')
let dialogFormVisible = ref(false)

interface LinkItem {
  value: string
  link: string
}

const form = reactive({
  pname: '',
})

const links = ref<LinkItem[]>([])

const querySearch = (queryString: string, cb) => {
  const results = queryString
      ? links.value.filter(createFilter(queryString))
      : links.value
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
      p.push({value: res.data[i].pname, link: res.data[i].pid})
    }
  })
  return p
}

const tableData = shallowReactive([])
$http.get('/getIndirect').then(res => {
  for (let i = 0; i < res.data.length; i++) {
    tableData.push({
      id: res.data[i].id,
      type: res.data[i].type1 + res.data[i].type2,
      note: res.data[i].note,
      money: res.data[i].money,
      project: res.data[i].project,
      date: res.data[i].date
    })
  }
})

const handleSelect = (item: LinkItem) => {
  tableData.splice(0,tableData.length)
  $http.get('/getIndirectByPid?pid='+toRaw(item).link).then(res => {
    for (let i = 0; i < res.data.length; i++) {
      tableData.push({
        id: res.data[i].id,
        type: res.data[i].type1 + res.data[i].type2,
        note: res.data[i].note,
        money: res.data[i].money,
        project: res.data[i].project,
        date: res.data[i].date
      })
    }
  })
}

function show() {
  form.pname = "";
  tableData.splice(0, tableData.length);
  $http.get('/getIndirect').then(res => {
    for (let i = 0; i < res.data.length; i++) {
      tableData.push({
        id: res.data[i].id,
        type: res.data[i].type1 +' '+ res.data[i].type2,
        note: res.data[i].note,
        money: res.data[i].money,
        project: res.data[i].project,
        date: res.data[i].date
      })
    }
  })
}

const handleIconClick = (ev: Event) => {
  //console.log(ev)
}

onMounted(() => {
  links.value = loadAll()
})

const cancel = (val) => {
  dialogFormVisible.value = false;
}

</script>

<style>
.my-autocomplete li {
  line-height: normal;
  padding: 7px;
}

.my-autocomplete li .name {
  text-overflow: ellipsis;
  overflow: hidden;
}

.my-autocomplete li .addr {
  font-size: 12px;
  color: #b4b4b4;
}

.my-autocomplete li .highlighted .addr {
  color: #ddd;
}
</style>
