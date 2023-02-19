<template>
  <div>
    <!--物料详情 -->
    <el-card>
      <div class="crumbs">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item>
            <i class="el-icon-lx-calendar"></i> 表单
          </el-breadcrumb-item>
          <el-breadcrumb-item>物料详情</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="container">
        <div class="handle-box">
          <div class="table-header">
            <el-form>
              <el-form-item>
                <el-row>
                  <div class="header-right">
                    <el-button type="primary" @click="dialogFormVisible1 = true" style="height: 30px ;margin: 10px;">新增
                    </el-button>
                  </div>
                </el-row>
              </el-form-item>
            </el-form>
            <el-table :data="tableData1" style="width: 100%">
              <el-table-column prop="id" label="物料编号"></el-table-column>
              <el-table-column prop="name" label="名称"></el-table-column>
              <el-table-column prop="model" label="规格"></el-table-column>
              <el-table-column prop="unit" label="单位"></el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 物料采购-->
    <el-card>
      <div class="crumbs">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item>
            <i class="el-icon-lx-calendar"></i> 表单
          </el-breadcrumb-item>
          <el-breadcrumb-item>物料采购</el-breadcrumb-item>
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
                    >
                      <template #suffix>
                        <el-icon class="icon-mya-Group46" @click="handleIconClick2">
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
            <el-table :data="tableData2" style="width: 100%">
              <el-table-column prop="id" label="记录编号"></el-table-column>
              <el-table-column prop="pname" label="所属项目"></el-table-column>
              <el-table-column prop="mname" label="材料名"></el-table-column>
              <el-table-column prop="cost" label="费用(万元)"></el-table-column>
              <el-table-column prop="supplier" label="供应商"></el-table-column>
              <el-table-column prop="date" label="日期"></el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </el-card>

    <el-dialog v-model="dialogFormVisible1" title="新增材料">
      <MaterialAdd @cancel="cancel1"></MaterialAdd>
    </el-dialog>

    <el-dialog v-model="dialogFormVisible2" title="材料支出记录">
      <MaterialPayAdd @cancel="cancel2"></MaterialPayAdd>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {getCurrentInstance, inject, onMounted, reactive, ref, shallowReactive, toRaw} from "vue";
import {Edit} from '@element-plus/icons-vue'
import {ElMessage} from "element-plus";
import MaterialPayAdd from "./Dialog/MaterialPayAdd.vue";
import MaterialAdd from "./Dialog/MaterialAdd.vue";

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


//物料
const tableData1 = shallowReactive([])
$http.get('/getMaterial').then(res => {
  for (let i = 0; i < res.data.length; i++) {
    tableData1.push({
      id: res.data[i].id,
      name: res.data[i].name,
      model: res.data[i].model,
      unit: res.data[i].unit,
    })
  }
})
const cancel1 = (val) => {
  dialogFormVisible1.value = false;
}


//物料购买
const form2 = reactive({
  pname: '',
})

const tableData2 = shallowReactive([])
$http.get('/getMaterialPay').then(res => {
  for (let i = 0; i < res.data.length; i++) {
    tableData2.push({
      id: res.data[i].id,
      pname: res.data[i].project,
      mname: res.data[i].material,
      cost: res.data[i].money,
      supplier: res.data[i].supplier,
      date: res.data[i].date
    })
  }
})

const handleSelect2 = (item: LinkItem) => {
  //console.log(toRaw(item).id)
  tableData2.splice(0, tableData2.length);
  $http.get('/getMaterialPayByPid?pid=' + toRaw(item).id).then(res => {
    for (let i = 0; i < res.data.length; i++) {
      tableData2.push({
        id: res.data[i].id,
        pname: res.data[i].project,
        mname: res.data[i].material,
        cost: res.data[i].money,
        supplier: res.data[i].supplier,
        date: res.data[i].date
      })
    }
  })
}

function show2() {
  form2.pname = ""
  $http.get('/getMaterialPay').then(res => {
    tableData2.splice(0, tableData2.length);
    for (let i = 0; i < res.data.length; i++) {
      tableData2.push({
        id: res.data[i].id,
        pname: res.data[i].project,
        mname: res.data[i].material,
        cost: res.data[i].money,
        supplier: res.data[i].supplier,
        date: res.data[i].date
      })
    }
  })
}

const handleIconClick2 = (ev: Event) => {
  //console.log(ev)
}

const cancel2 = (val) => {
  dialogFormVisible2.value = false;
}

</script>

<style scoped>

</style>