<template>
  <div>
    <el-form :model="form">
      <el-form-item label="材料" :label-width="formLabelWidth" prop="name">
        <el-autocomplete
            v-model="form.name"
            :fetch-suggestions="querySearch1"
            popper-class="my-autocomplete"
            placeholder="Please input"
            @select="handleSelect1"
        >
          <template #default="{ item }">
            <div class="value">{{ item.value }}</div>
            <span class="link">型号:{{ item.link }}</span>
          </template>
        </el-autocomplete>
      </el-form-item>

      <el-form-item label="供应商" :label-width="formLabelWidth">
        <el-input v-model="form.supplier"></el-input>
      </el-form-item>

      <el-form-item label="支出金额" :label-width="formLabelWidth">
        <el-col>
          <el-input-number v-model="money" :precision="2" :step="0.1"/>
          <span>万元</span>
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
import {reactive, ref, onMounted, getCurrentInstance, inject, toRaw} from "vue";
import {ElMessage} from "element-plus";

const currentInstance = getCurrentInstance();
const {$http} = currentInstance.appContext.config.globalProperties;
const reload = inject('reload')
const formLabelWidth = '100px'

const money = ref(1);
const form = reactive({
  name: '',
  id: '',
  supplier:'',
  pname: '',
  pid: '',
  date: '',
})

const state = ref('')

//搜索材料
interface Emp {
  value: string
  link: string
}

const links = ref<Emp[]>([])

const loadAll1 = () => {
  let m = []
  $http.get('/getMaterial').then(res => {
    for (let i = 0; i < res.data.length; i++) {
      m.push({value: res.data[i].name+res.data[i].model, link: res.data[i].id})
    }
  })
  return m
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
}

const submit = () => {
  $http.post('/addMaterialPay', {
    material:form.id,
    supplier:form.supplier,
    money:money.value,
    project: form.pid,
    date: form.date,
  }).then(res => {
    if (res.data.code == 200) {
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