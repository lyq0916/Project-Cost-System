<template>
  <div>
    <!--设备详情 -->
    <el-card>
      <div class="crumbs">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item>
            <i class="el-icon-lx-calendar"></i> 表单
          </el-breadcrumb-item>
          <el-breadcrumb-item>设备详情</el-breadcrumb-item>
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
                    <el-button type="primary" @click="dialogFormVisible1 = true" style="height: 30px ;margin: 10px;">新增
                    </el-button>
                  </div>
                </el-row>
              </el-form-item>
            </el-form>
            <el-table :data="tableData1" style="width: 100%">
              <el-table-column prop="id" label="设备编号"></el-table-column>
              <el-table-column prop="name" label="名称"></el-table-column>
              <el-table-column prop="note" label="备注"></el-table-column>
              <el-table-column prop="pname" label="所属项目"></el-table-column>
              <el-table-column prop="buy" label="购买日期"></el-table-column>
              <el-table-column prop="use" label="使用日期"></el-table-column>
              <el-table-column prop="money" label="设备价格(万元)"></el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 设备维修保养支出-->
    <el-card>
      <div class="crumbs">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item>
            <i class="el-icon-lx-calendar"></i> 表单
          </el-breadcrumb-item>
          <el-breadcrumb-item>设备维修保养支出</el-breadcrumb-item>
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
              <el-table-column prop="ename" label="设备名"></el-table-column>
              <el-table-column prop="note" label="备注"></el-table-column>
              <el-table-column prop="cost" label="费用(万元）"></el-table-column>
              <el-table-column prop="date" label="日期"></el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </el-card>

    <el-dialog v-model="dialogFormVisible1" title="新增机械设备">
      <EquipAdd @cancel="cancel1"></EquipAdd>
    </el-dialog>

    <el-dialog v-model="dialogFormVisible2" title="设备支出记录">
      <EquipPayAdd @cancel="cancel2"></EquipPayAdd>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {getCurrentInstance, inject, onMounted, reactive, ref, shallowReactive, toRaw} from "vue";
import {Edit} from '@element-plus/icons-vue'
import {ElMessage} from "element-plus";
import EquipPayAdd from "./Dialog/EquipPayAdd.vue";
import EquipAdd from "./Dialog/EquipAdd.vue";

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


//设备详情
const form1 = reactive({
  pname: '',
})

const tableData1 = shallowReactive([])
$http.get('/getEquip').then(res => {
  for (let i = 0; i < res.data.length; i++) {
    tableData1.push({
      id: res.data[i].id,
      name: res.data[i].name,
      note: res.data[i].note,
      pname: res.data[i].pid,
      buy: res.data[i].buy,
      use: res.data[i].use,
      money:res.data[i].money
    })
  }
})

const handleSelect1 = (item: LinkItem) => {
  //console.log(toRaw(item).id)
  tableData1.splice(0, tableData1.length);
  $http.get('/getEquipByPid?pid=' + toRaw(item).id).then(res => {
    for (let i = 0; i < res.data.length; i++) {
      tableData1.push({
        id: res.data[i].id,
        name: res.data[i].name,
        note: res.data[i].note,
        pname: res.data[i].pid,
        buy: res.data[i].buy,
        use: res.data[i].use
      })
    }
  })
}


function show1() {
  form1.pname = ""
  tableData1.splice(0, tableData1.length);
  $http.get('/getEquip').then(res => {
    for (let i = 0; i < res.data.length; i++) {
      tableData1.push({
        id: res.data[i].id,
        name: res.data[i].name,
        note: res.data[i].note,
        pname: res.data[i].pid,
        buy: res.data[i].buy,
        use: res.data[i].use
      })
    }
  })
}


const handleIconClick1 = (ev: Event) => {
  //console.log(ev)
}

const cancel1 = (val) => {
  dialogFormVisible1.value = false;
}

//设备维修保养支出
const form2 = reactive({
  pname: '',
})

const tableData2 = shallowReactive([])
$http.get('/getEquipPay').then(res => {
  for (let i = 0; i < res.data.length; i++) {
    tableData2.push({
      id: res.data[i].id,
      ename: res.data[i].ename,
      note: res.data[i].note,
      cost: res.data[i].money,
      date: res.data[i].date
    })
  }
})

const handleSelect2 = (item: LinkItem) => {
  //console.log(toRaw(item).id)
  tableData2.splice(0, tableData1.length);
  $http.get('/getEquipPayByPid?pid=' + toRaw(item).id).then(res => {
    for (let i = 0; i < res.data.length; i++) {
      tableData2.push({
        id: res.data[i].id,
        ename: res.data[i].ename,
        note: res.data[i].note,
        cost: res.data[i].money,
        date: res.data[i].date
      })
    }
  })
}


function show2() {
  form2.pname = ""
  tableData2.splice(0, tableData2.length);
  $http.get('/getEquipPay').then(res => {
    for (let i = 0; i < res.data.length; i++) {
      tableData2.push({
        id: res.data[i].id,
        ename: res.data[i].ename,
        note: res.data[i].note,
        cost: res.data[i].money,
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