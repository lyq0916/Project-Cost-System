<template>
  <div>
    <el-form :model="form">
      <el-form-item label="材料名" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.name" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="型号" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.model" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="单位" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.unit" autocomplete="off"/>
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

const form = reactive({
  name: '',
  model:'',
  unit: '',
})

const formLabelWidth = '140px'

const emit = defineEmits(['cancel'])
const cancel = () => {
  emit('cancel')
}

const submit = () => {
  console.log(form)
  $http.post('/addMaterial',{
    name:form.name,
    model:form.model,
    unit:form.unit,
  }).then(res => {
    if(res.data.code == 200) {
      ElMessage.success("添加成功")
      emit('cancel')
      reload()
    }
    else ElMessage.error("添加失败")
  })
}
</script>

<style scoped>
.dialog-footer {
  position: absolute;
  right: 5px;
}
</style>