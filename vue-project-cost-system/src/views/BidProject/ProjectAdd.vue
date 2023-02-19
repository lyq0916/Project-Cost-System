<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-calendar"></i> 表单
        </el-breadcrumb-item>
        <el-breadcrumb-item>新建项目</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="form-box">
        <el-form ref="formRef" :rules="rules" :model="form" label-width="120px">
          <el-form-item label="项目名称" prop="name">
            <el-input v-model="form.name"></el-input>
          </el-form-item>

          <el-form-item label="招标编号" prop="name">
            <el-input v-model="form.bnumber"></el-input>
          </el-form-item>

          <el-form-item label="报备日期" prop="date">
            <el-date-picker
                type="date"
                placeholder="请选择日期"
                v-model="form.date"
                value-format="YYYY-MM-DD"
                format="YYYY-MM-DD"
                style="width: 100%;">
            </el-date-picker>
            <el-col class="line" :span="2"></el-col>
          </el-form-item>

          <el-form-item label="省/市/区" prop="addr">
            <elui-china-area-dht @change="onChange" v-model="form.addr"></elui-china-area-dht>
          </el-form-item>

          <el-form-item label="项目类型" prop="type">
            <el-select v-model="form.type" class="m-2" placeholder="Select">
              <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="材料预算">
            <el-col>
              <el-input-number v-model="m" :precision="2" :step="0.1" style="margin-right: 5px"/>
              <span>万元</span>
            </el-col>
          </el-form-item>

          <el-form-item label="机械设备预算">
            <el-col>
              <el-input-number v-model="e" :precision="2" :step="0.1" style="margin-right: 5px"/>
              <span>万元</span>
            </el-col>
          </el-form-item>

          <el-form-item label="人工费用预算">
            <el-col>
              <el-input-number v-model="l" :precision="2" :step="0.1" style="margin-right: 5px"/>
              <span>万元</span>
            </el-col>
          </el-form-item>

          <el-form-item label="其他预算">
            <el-col>
              <el-input-number v-model="o" :precision="2" :step="0.1" style="margin-right: 5px"/>
              <span>万元</span>
            </el-col>
          </el-form-item>

          <el-form-item label="间接支出预算">
            <el-col>
              <el-input-number v-model="i" :precision="2" :step="0.1" style="margin-right: 5px"/>
              <span>万元</span>
            </el-col>
          </el-form-item>

          <el-form-item label="总预算">
            <el-col>
              <el-input-number v-model="num" :precision="2" :step="0.1" style="margin-right: 5px" :disabled="true"/>
              <span>万元</span>
            </el-col>
          </el-form-item>

          <el-form-item label="项目负责人" prop="header">
            <el-select
                v-model="form.header"
                filterable
                remote
                reserve-keyword
                placeholder="请输入项目负责人"
                :remote-method="remoteMethod"
                :loading="loading"
            >
              <el-option
                  v-for="item in options1"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="onSubmit">表单提交</el-button>
            <el-button @click="onReset">重置表单</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>

</template>

<script lang="ts" setup>
import {ref, reactive, getCurrentInstance, computed, onMounted, shallowReactive} from "vue";
import {ElMessage} from "element-plus";
// 参数取值
import {EluiChinaAreaDht} from 'elui-china-area-dht'

const chinaData = new EluiChinaAreaDht.ChinaArea().chinaAreaflat

const currentInstance = getCurrentInstance();
const {$http} = currentInstance.appContext.config.globalProperties;

//预算--五大类
const m = ref(1);
const e = ref(1);
const l = ref(1);
const o = ref(1);
const i = ref(1);
const num = computed(() => {
  return m.value + e.value + l.value + o.value + i.value;
})

//项目类型
const options = [
  {
    value: '交通运输',
    label: '交通运输',
  },
  {
    value: '住宅建筑',
    label: '住宅建筑',
  },
  {
    value: '商业办公',
    label: '商业办公',
  },
  {
    value: '医疗卫生',
    label: '医疗卫生',
  },
  {
    value: '环保节能',
    label: '环保节能',
  },
  {
    value: '文化体育',
    label: '文化体育',
  },
  {
    value: '服务领域',
    label: '服务领域',
  },
  {
    value: '农业水利',
    label: '农业水利',
  },
  {
    value: '其他领域',
    label: '其他领域',
  },
];

const rules = {
  name: [
    {required: true, message: "请输入表单名称", trigger: "blur"},
  ],
  addr: [
    {required: true, message: "请输入项目地址", trigger: "blur"},
  ],
  header: [
    {required: true, message: "请选择项目负责人", trigger: "blur"},
  ]
};

//表单
const formRef = ref(null);
const form = reactive({
  name: "",
  bnumber: "",
  date: "",
  addr: "",
  type: "",
  budget: "1",
  desc: "",
  options: [],
  header: "",
});

//省市区
let address = "";

function onChange(e) {
  const one = chinaData[e[0]]
  const two = chinaData[e[1]]
  const three = chinaData[e[2]]
  address = one.label + two.label + three.label;
}

//选择项目负责人
interface ListItem {
  value: string
  label: string
}

const list = ref<ListItem[]>([])
const options1 = ref<ListItem[]>([])
const value = ref<string[]>([])
const loading = ref(false)
const states = shallowReactive([])
onMounted(() => {
  $http.get('/getUsernames').then((res) => {
    for(let i=0;i<res.data.length;i++) {
      states.push(res.data[i])
    }
    list.value = states.map((item) => {
      return { value: `${item}`, label: `负责人:${item}` }
    })
  })
})

const remoteMethod = (query: string) => {
  if (query) {
    loading.value = true
    setTimeout(() => {
      loading.value = false
      options1.value = list.value.filter((item) => {
        return item.label.toLowerCase().includes(query.toLowerCase())
      })
    }, 200)
  } else {
    options1.value = []
  }
}

// 提交表单，保存到数据库
const onSubmit = () => {
  // 表单校验
  formRef.value.validate((valid) => {
    if (valid) {
      $http.post('/getusr',{
        username:form.header
      }).then((res) => {
        //console.log(res.data.id)
        var date1 = new Date(Date.parse(form.date));
        $http.post('/projectadd', {
          pname: form.name,
          bnumber: form.bnumber,
          address: address,
          type: form.type,
          budget: num.value,
          mbudget: m.value,
          ebudget: e.value,
          lbudget: l.value,
          obudget: o.value,
          ibudget: i.value,
          date: date1,
          header:res.data.id,
        }).then((res => {
          if (res.data.code == 200) {
            ElMessage.success("提交成功");
            formRef.value.resetFields();
          } else {
            ElMessage.error("提交失败");
          }
        }))
      })
    } else {
      return false;
    }
  });
};

// 重置
const onReset = () => {
  formRef.value.resetFields();
};

</script>

<style scoped>


</style>