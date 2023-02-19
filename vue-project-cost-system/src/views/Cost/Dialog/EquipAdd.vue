<template>
  <div>
    <el-form :model="form">
      <el-form-item label="设备名" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.name" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="备注" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.note" autocomplete="off"/>
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
      <el-form-item label="购买日期" :label-width="formLabelWidth" prop="date">
        <el-date-picker
            type="date"
            placeholder="请选择日期"
            v-model="form.buy"
            value-format="YYYY-MM-DD"
            format="YYYY-MM-DD"
            style="width: 250px;">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="正式使用日期" :label-width="formLabelWidth" prop="date">
        <el-date-picker
            type="date"
            placeholder="请选择日期"
            v-model="form.use"
            value-format="YYYY-MM-DD"
            format="YYYY-MM-DD"
            style="width: 250px;">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="设备费用" :label-width="formLabelWidth">
        <el-col>
          <el-input-number v-model="money" :precision="2" :step="0.1"/>
          <span>万元</span>
        </el-col>
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

const money = ref(1);
const form = reactive({
  name: '',
  note: '',
  pid: '',
  pname: '',
  buy:'',
  use:'',
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
  $http.post('/addEquip', {
    name: form.name,
    note: form.note,
    pid: form.pid,
    buy:form.buy,
    use:form.use,
    money:money.value
  }).then(res => {
    if (res.data.code == 200) {
      ElMessage.success("添加成功")
      emit('cancel')
      reload()
    } else ElMessage.error("添加失败")
  })
}
</script>

<style scoped>
.dialog-footer {
  position: absolute;
  right: 5px;
}
</style>