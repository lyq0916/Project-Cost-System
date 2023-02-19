<template>
  <el-card>
    <template #header>实际支出与预算对比
      <div class="clearfix">
      </div>
    </template>
    <div :id="p"
         :style="{width: '900px', height: '400px',margin:'10px'}"
         ref="chart"
    ></div>
  </el-card>
</template>

<script lang="ts" setup>
import {getCurrentInstance, ref, shallowReactive} from "vue";
import * as echarts from "echarts";

const currentInstance = getCurrentInstance();
const {$http} = currentInstance.appContext.config.globalProperties;

let p = ref(null);
p = Math.round(Math.random() * 100 + 200)

const props = defineProps({
  pid: Number,
})
const cost = shallowReactive([])
const budget = shallowReactive([])
const dataY = shallowReactive([])

let option;


$http.get('/getcbByType?pid=' + props.pid).then(res => {
  budget.push(res.data["材料"]["预算"], res.data["机械设备"]["预算"], res.data["人工"]["预算"], res.data["其他"]["预算"], res.data["间接支出"]["预算"])
  cost.push(res.data["材料"]["实际"], res.data["机械设备"]["实际"], res.data["人工"]["实际"], res.data["其他"]["实际"], res.data["间接支出"]["实际"])
  dataY.push((res.data["材料"]["实际"] / res.data["材料"]["预算"]).toFixed(2), (res.data["机械设备"]["实际"] / res.data["机械设备"]["预算"]).toFixed(2),
      (res.data["人工"]["实际"] / res.data["人工"]["预算"]).toFixed(2), (res.data["其他"]["实际"] / res.data["其他"]["预算"]).toFixed(2),
      (res.data["间接支出"]["实际"] / res.data["间接支出"]["预算"]).toFixed(2)
  )
  let myChart = echarts.init(document.getElementById(p));
  option = {
    tooltip: {
      trigger: 'axis',
    },
    legend: {
      bottom: 0
    },
    xAxis: [
      {
        type: 'category',
        data: ['材料', '机械设备', '人工', '其他', '间接支出']
      }
    ],
    yAxis: [
      {
        type: 'value',
        splitLine: {
          show: false,
        },
        axisLabel: {
          formatter: '{value}'
        }
      },
      /*实际/预算 百分比*/
      {
        type: 'value',
        splitLine: {
          show: false,
        },
        name: '(比率)',
       /* max: '100',
        min: '0',*/
        scale: false,
        position: 'right',
        axisLabel: {
          show: true,
          formatter: '{value} ', //右侧Y轴文字显示
        },
      },
    ],
    series: [
      {
        name: '实际支出',
        type: 'bar',
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {offset: 0, color: '#35EB81'},
            {offset: 1, color: 'rgba(53,235,129,.2)'}
          ])
        },
        data: cost
      },
      {
        name: '预算',
        type: 'bar',
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {offset: 0, color: '#32C1FF'},
            {offset: 1, color: 'rgba(17,75,183,0.20)'}
          ])
        },
        data: budget,
      },
      {
        name: '使用率 %',
        type: 'line',
        yAxisIndex: 1, //使用的 y 轴的 index，在单个图表实例中存在多个 y轴的时候有用
        smooth: true, //平滑曲线显示

        symbol: 'circle', //标记的图形为实心圆
        symbolSize: 8, //标记的大小
        itemStyle: {
          normal: {
            color: '#faa755',
            borderColor: 'rgba(255, 165, 0, 0.5)', //圆点透明 边框
            borderWidth: 5,
          },
        },
        lineStyle: {
          color: '#faa755',
        },

        data: dataY,
      },
      {
        text: '单位：万元',
        bottom: 20,
        right: 20,
        textStyle: {
          fontSize: 14,
          color: '#666666',
          fontWeight: 400
        }
      },
    ]
  };
  myChart.setOption(option);
  myChart.resize();
})

</script>

<style scoped>

</style>