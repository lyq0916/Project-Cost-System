<template>
  <el-card>
    <template #header>项目进度
      <div class="clearfix">
      </div>
    </template>
    <div :id="p"
         :style="{width: '350px', height: '350px',margin:'10px'}"
         ref="chart"
    ></div>
  </el-card>
</template>

<script lang="ts" setup>
import * as echarts from "echarts";
import {getCurrentInstance, ref, shallowReactive} from "vue";
import 'echarts-liquidfill'
import moment from "moment";
const currentInstance = getCurrentInstance();
const {$http} = currentInstance.appContext.config.globalProperties;
const props = defineProps({
  pid: Number,
})

let p = ref(null);
p = Math.round(Math.random() * 100 + 400)

let data: string
let option;

function getDaysBetween(start,end){
  const sDate=Date.parse(start)
  const eDate=Date.parse(end)
  if(sDate>=eDate) return 0
  return (eDate-sDate)/(1*24*60*60*1000)
}

$http.get('/findConstByPid?pid='+props.pid).then((res) => {
  //console.log(getDaysBetween(res.data.estart,res.data.eend))
  if(res.data.start==null){
    data='0'
  }else if(res.data.end!=null){
    data='1'
  }else{
    //console.log(getDaysBetween(res.data.start,moment().format("YYYY-MM-DD")))
    data=(getDaysBetween(res.data.start,moment().format("YYYY-MM-DD"))/getDaysBetween(res.data.estart,res.data.eend)).toFixed(2)
  }

  console.log()
  option = {
    series: [{
      type: 'liquidFill',
      radius: '50%',
      center: ['50%','50%'],
      data: [data],
      color: 'rgba(67,209,100,1)',
      label: {
        normal: {
          color: '#fff',
          insideColor: 'transparent',
          textStyle: {
            fontSize: 16,
            fontWeight: 'bold',
            fontFamily: 'Microsoft YaHei'
          }
        }
      },
      outline: {
        show: true,
        borderDistance: 5,
        itemStyle: {
          borderColor: 'rgba(67,209,100,1)',
          borderWidth: 2
        }
      },
      backgroundStyle: {
        color: 'rgba(67,209,100,.3)'
      }
    }]

  };
  let myChart = echarts.init(document.getElementById(p));
  myChart.setOption(option);
  myChart.resize();
})
</script>

<style scoped>

</style>