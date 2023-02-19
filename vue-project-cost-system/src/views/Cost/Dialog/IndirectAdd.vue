<template>
  <div>
    <el-form :model="form">
      <el-form-item label="间接支出类型" :label-width="formLabelWidth" prop="name">
        <el-cascader :options="options" :show-all-levels="false" clearable v-model="form.type" @change="handleChange"/>
      </el-form-item>
      <el-form-item label="备注" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.note" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="支出金额" :label-width="formLabelWidth">
        <el-col>
          <el-input-number v-model="money" :precision="2" :step="0.1"/>
          <span>万元</span>
        </el-col>
      </el-form-item>
      <el-form-item label="所属项目" :label-width="formLabelWidth" prop="id">
        <el-autocomplete
            v-model="form.pname"
            :fetch-suggestions="querySearch"
            popper-class="my-autocomplete"
            placeholder="Please input"
            @select="handleSelect"
        >
          <template #default="{ item }">
            <div class="value">{{ item.value }}</div>
            <span class="link">项目编号:{{ item.id }}</span>
          </template>
        </el-autocomplete>
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
      <el-form-item>
         <span class="dialog-footer">
        <el-button @click="cancel">Cancel</el-button>
        <el-button type="primary" @click="submit">Confirm</el-button>
      </span>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import {getCurrentInstance, inject, onMounted, reactive, ref, toRaw} from "vue";
import {ElMessage} from "element-plus";
const currentInstance = getCurrentInstance();
const {$http} = currentInstance.appContext.config.globalProperties;
const reload = inject('reload')

const options = [
  {
    value: '规费',
    label: '规费',
    children: [
      {
        value: '工程排污费',
        label: '工程排污费',
      },
      {
        value: '社会保险费',
        label: '社会保险费',
      },
      {
        value: '住房公积金',
        label: '住房公积金',
      },
    ]
  },
  {
    value: '企业管理费',
    label: '企业管理费',
    children: [
      {
        value: '管理人员工资',
        label: '管理人员工资',
      },
      {
        value: '差旅交通费',
        label: '差旅交通费',
      },
      {
        value: '固定资产使用费',
        label: '固定资产使用费',
      },
      {
        value: '工具用具使用费',
        label: '工具用具使用费',
      },
      {
        value: '劳动保险费',
        label: '劳动保险费',
      },
      {
        value: '工会经费',
        label: '工会经费',
      },
      {
        value: '职工教育经费',
        label: '职工教育经费',
      },
      {
        value: '财产保险费',
        label: '财产保险费',
      },
      {
        value: '财务费',
        label: '财务费',
      },
      {
        value: '税金',
        label: '税金',
      },
      {
        value: '其他',
        label: '其他',
      },
    ]
  }
]

const money = ref(1);
const form = reactive({
  type: '',
  note:'',
  pid: '',
  pname: '',
  date:''
})

const formLabelWidth = '140px'

//选择项目
interface LinkItem {
  value: string
  id: string
}

const state = ref('')
const p = ref<LinkItem[]>([])

const querySearch = (queryString: string, cb) => {
  const results = queryString
      ? p.value.filter(createFilter(queryString))
      : p.value
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

const handleSelect = (item: LinkItem) => {
  //console.log(toRaw(item).id)
  form.pid = toRaw(item).id
}

onMounted(() => {
  p.value = loadAll()
})

const emit = defineEmits(['cancel'])
const cancel = () => {
  emit('cancel')
}

const submit = () => {
  console.log(form)
  $http.post('/addIndirect',{
    type1:toRaw(form.type)[0],
    type2:toRaw(form.type)[1],
    note:form.note,
    money:money.value,
    project:form.pid,
    date:form.date,
  }).then(res => {
    if(res.data.code == 200) {
      ElMessage.success("添加成功")
      emit('cancel')
      reload()
    }
    else ElMessage.error("添加失败")
  })
}

const handleChange=()=>{
  console.log()
}
</script>

<style scoped>
.dialog-footer {
  position: absolute;
  right: 5px;
}
</style>