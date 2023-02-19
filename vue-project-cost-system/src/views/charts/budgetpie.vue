<template>
  <el-card>
    <template #header>
      <div class="clearfix">预算费用
      </div>
    </template>
    <el-row>
      <el-col>
        <div :id="p"
             :style="{width: '400px', height: '300px',margin:'10px'}"
             ref="chart"
        ></div>
      </el-col>
    </el-row>
  </el-card>
</template>

<script lang="ts" setup>
import * as echarts from "echarts";
import {getCurrentInstance, ref, shallowReactive} from "vue";
const currentInstance = getCurrentInstance();
const {$http} = currentInstance.appContext.config.globalProperties;

const props = defineProps({
  pid: Number,
})

let p = ref(null);
p = Math.round(Math.random() * 100 + 300)

var colors = [
  '#58b2ff', '#68d4b2', '#7380ff', '#fdd56a',
  '#fdb36a', '#fd866a', '#9e87ff'
];
var data = [
 /* { value: 55, name: '煤' },
  { value: 38, name: '电' },
  { value: 32, name: '油' },
  { value: 30, name: '其他' },
  { value: 28, name: '气' },*/
];
var total = data.reduce((p, v) => {
  return p + v.value;
}, 0);

let option;
option = {
  tooltip: {
    trigger: 'item',
    formatter: '{b} : {c}万元 ({d}%)',
  },
  color: colors,
  calculable: true,
  legend: {
    orient: 'vertical',
    itemWidth: 18,
    itemHeight: 6,
    right: '5%',
    align: 'left',
    top: 'middle',
    textStyle: {
      fontSize: 12,
      fontFamily: 'PingFangSC-Regular, PingFang SC',
      fontWeight: 400,
      color: '#0D1134',
      lineHeight: 17,
    },
  },
  series: [
    {
      type: 'pie',
      radius: ['30%', '45%'],
      center: ['35%', '50%'],
      //roseType: 'radius',
      label: {
        show: false,
        color: 'inherit',
        position: 'outside',
        fontSize: 14,
        formatter:"{b} {d}%",
      },
      data: data,
    },
  ],
};
 /* let newPromise = new Promise((resolve) => {
       resolve()
     })
     //然后异步执行echarts的初始化函数
     newPromise.then(() => {
       let myChart = echarts.init(document.getElementById(p));
       myChart.setOption(option);
       myChart.resize();
     })*/
$http.get('/getprojectbyid?pid='+props.pid).then(res=>{
  data.push({value:res.data.mbudget,name:"材料"},{value:res.data.ebudget,name:"机械设备"},
      {value:res.data.lbudget,name:"人工"},{value:res.data.obudget,name:"其他"},{value:res.data.ibudget,name:"间接支出"}
  )
  let myChart = echarts.init(document.getElementById(p));
  myChart.setOption(option);
  myChart.resize();
})

</script>

<style scoped>

</style>