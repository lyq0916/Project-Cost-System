<template>
  <el-card>
    <template #header>支出
      <div class="clearfix">
      </div>
    </template>
    <div :id="p"
         :style="{width: '900px', height: '600px',margin:'10px'}"
         ref="chart"
    ></div>
  </el-card>
</template>
<script lang="ts" setup>
import {getCurrentInstance, ref, shallowReactive} from "vue";
import * as echarts from "echarts";
const currentInstance = getCurrentInstance();
const {$http} = currentInstance.appContext.config.globalProperties;

//父子传值
const props = defineProps({
  pid: Number,
})

let myChart;
//解决一个页面复用chart问题，随机生成id
let p = ref(null);
p = Math.round(Math.random() * 100 + 100)

//绘制预算线
const b = shallowReactive([])

// tooltip
const tooltip = {
  trigger: 'axis',
  textStyle: {fontSize: '100%'},
  formatter: (params) => {
    let rander = params
        .map(
            (item) =>
                // item.seriesType !== 'pictorialBar'
                //     ? `<div>${item.seriesName}: ${item.seriesType !== 'line' ? item.value : item.value + '%'}</div>`
                //     : ''
                `<div>${item.seriesName}: ${item.value}</div>`
        ).join('');
    return `${rander}`;
  },
};
const legend = {
  data: ['总支出'],
  textStyle: {fontSize: 14, color: '#000'},
  itemWidth: 25,
  itemHeight: 15,
  itemGap: 15,
  right: '10%',
};
const grid = {top: '5%', left: '10%', right: '6%', bottom: '25%'};

$http.get('/getSumByDate?pid=' + props.pid).then((res) => {
  console.log(res)
  for(let i=0;i<res.data.length;i++){
    b.push([res.data[i]["date"],res.data[i]["sum"]])
  }
    //注入数据后再绘图
    // xAxis
    const xAxis = {
      type: 'time',
      axisLine: {lineStyle: {color: '#C7DEFF'}},
      splitLine: {lineStyle: {color: '#C7DEFF'}},
      axisLabel: {
        rotate: 40,
        textStyle: {fontSize: 12, color: '#2B6BD1'},
        //formatter: '{yyyy}-{MM}-{dd}',
        formatter: function (value, index) {
          // 格式化成月/日，只在第一个刻度显示年份
          var date = new Date(value);
          var texts = [(date.getMonth() + 1), date.getDate()];
          if (index === 0) {
            texts.unshift(date.getFullYear());
          }
          return texts.join('/');
        }
      },
    };

    // yAxis
    const yAxis = [
      {
        axisTick: {show: false},
        axisLine: {lineStyle: {color: '#fff'}},
        splitLine: {lineStyle: {color: '#C7DEFF'}, interval: 'auto'},
        axisLabel: {textStyle: {fontSize: 12, color: '#2B6BD1'}},
      },
      {
        show: true,
        max: 100,
        interval: 'auto',
        splitLine: {show: false},
        axisLine: {show: false},
        axisTick: {show: false},
        axisLabel: {
          textStyle: {fontSize: 12, color: '#000'},
          formatter: (params) => {
            return `${params}%`;
          },
        },
      },
    ];

    $http.get('/getprojectbyid?pid=' + props.pid).then((res) => {
      let budget=res.data.budget;
      // series
      const series = [
        {
          name: '总支出',
          type: 'line',
          smooth: true, //是否平滑
          showAllSymbol: true,
          symbol: 'circle',
          symbolSize: 10,
          lineStyle: {
            normal: {
              color: '#98DFE8',
              shadowColor: 'rgba(0, 0, 0, .3)',
              shadowBlur: 0,
              shadowOffsetY: 3,
              shadowOffsetX: 3,
            },
          },
          label: {
            show: true,
            position: 'top',
            textStyle: {
              color: '#98DFE8',
            },
          },
          itemStyle: {
            color: '#98DFE8',
            borderColor: '#fff',
            borderWidth: 1,
            shadowColor: 'rgba(0, 0, 0, .3)',
            shadowBlur: 0,
            shadowOffsetY: 2,
            shadowOffsetX: 2,
          },
          markLine: {
            "silent": true,
            "lineStyle": {
              "normal": {
                "width": "3",
                "color": "#98DFE8"
              }
            },
            "data": [
              {
                "yAxis": budget
              }
            ],
            "label": {
              "show": true,
              "normal": {
                "formatter": "材料预算",

              }
            }
          },
          data: b,
          areaStyle:{},
        },
      ];

      let option;
      option = {tooltip, xAxis, yAxis, series, grid, legend, backgroundColor: '#fff'};

      myChart = echarts.init(document.getElementById(p));
      myChart.setOption(option);
      window.addEventListener("resize", function () {
        myChart.resize();
      })
    })
  })
</script>

<style scoped>

</style>