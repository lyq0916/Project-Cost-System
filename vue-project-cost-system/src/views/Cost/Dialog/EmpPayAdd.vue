<template>
  <div>
    <el-form :model="form">
      <el-form-item label="员工" :label-width="formLabelWidth" prop="name">
        <el-autocomplete
            v-model="form.name"
            :fetch-suggestions="querySearch1"
            placeholder="Please input"
            @select="handleSelect1"
        />
      </el-form-item>

      <el-form-item label="基本工资" :label-width="formLabelWidth">
        <el-col>
          <el-input-number v-model="b" style="margin-right: 5px"/>
          <span>元</span>
        </el-col>
      </el-form-item>

      <el-form-item label="加班津贴" :label-width="formLabelWidth">
        <el-col>
          <el-input-number v-model="a" style="margin-right: 5px"/>
          <span>元</span>
        </el-col>
      </el-form-item>

      <el-form-item label="计件绩效" :label-width="formLabelWidth">
        <el-col>
          <el-input-number v-model="p" style="margin-right: 5px"/>
          <span>元</span>
        </el-col>
      </el-form-item>

      <el-form-item label="总预算" :label-width="formLabelWidth">
        <el-col>
          <el-input-number v-model="sum" style="margin-right: 5px" :disabled="true"/>
          <span>元</span>
        </el-col>
      </el-form-item>
      <el-form-item label="项目" :label-width="formLabelWidth" prop="id">
        <el-autocomplete
            v-model="form.pname"
            :fetch-suggestions="querySearch2"
            popper-class="my-autocomplete"
            placeholder="Please input"
            @select="handleSelect2"
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
import {computed, reactive, ref, onMounted, getCurrentInstance, inject, toRaw} from "vue";
import {ElMessage} from "element-plus";

const currentInstance = getCurrentInstance();
const {$http} = currentInstance.appContext.config.globalProperties;
const reload = inject('reload')

const b = ref(1000);
const a = ref(1000);
const p = ref(1000);

const formLabelWidth = '100px'
const sum = computed(() => {
  return b.value + a.value + p.value;
})

const form = reactive({
  name: '',
  id: '',
  pname: '',
  pid: '',
  date: '',
})

const state = ref('')

//搜索员工
interface Emp {
  value: string
  link: string
}

const links = ref<Emp[]>([])

const loadAll1 = () => {
  let emp = []
  $http.get('/getEmp').then(res => {
    for (let i = 0; i < res.data.length; i++) {
      emp.push({value: res.data[i].name, link: res.data[i].id})
    }
  })
  return emp
}

const querySearch1 = (queryString: string, cb) => {
  const results = queryString
      ? links.value.filter(createFilter1(queryString))
      : links.value
  cb(results)
}
const createFilter1 = (queryString: string) => {
  return (restaurant: Emp) => {
    return (
        restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
    )
  }
}

const handleSelect1 = (item: Emp) => {
  console.log(toRaw(item).link)
  form.id = toRaw(item).link
}

//搜索项目
interface project {
  value: string
  id: string
}

const project = ref<project[]>([])

const querySearch2 = (queryString: string, cb) => {
  const results = queryString
      ? project.value.filter(createFilter2(queryString))
      : project.value
  // call callback function to return suggestion objects
  cb(results)
}

const createFilter2 = (queryString) => {
  return (restaurant) => {
    return (
        restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
    )
  }
}
const loadAll2 = () => {
  let p = []
  $http.get('/projectshow').then(res => {
    for (let i = 0; i < res.data.length; i++) {
      p.push({value: res.data[i].pname, id: res.data[i].pid})
    }
  })
  return p
}

const handleSelect2 = (item: project) => {
  console.log(toRaw(item).id)
  form.pid = toRaw(item).id
}

onMounted(() => {
  links.value = loadAll1()
  project.value = loadAll2()
})


const emit = defineEmits(['cancel'])
const cancel = () => {
  emit('cancel')
  console.log((b.value/10000).toFixed(2))
}

const submit = () => {

  $http.post('/addEmpPay',{
    eid:form.id,
    basic:(b.value/10000).toFixed(2),
    allowance:(a.value/10000).toFixed(2),
    push:(p.value/10000).toFixed(2),
    money:(sum.value / 10000).toFixed(2),
    project:form.pid,
    date:form.date,
  }).then(res=>{
    if(res.data.code==200){
      if (res.data.code == 200) {
        ElMessage.success("提交成功");
        emit('cancel');
        reload();
      } else {
        ElMessage.error("提交失败");
      }
    }
  })
}

</script>

<style scoped>
.dialog-footer {
  position: absolute;
  right: 5px;
}
</style>