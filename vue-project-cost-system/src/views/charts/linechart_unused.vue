<template>
  <el-card>
    <template #header>
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

const props = defineProps({
  pid: Number,
})
let myChart;
//解决一个页面复用chart问题，随机生成id
let p = ref(null);
p = Math.round(Math.random() * 100 + 100)

let option;
// mock数据

const m = shallowReactive([])
let mb = shallowReactive(null)
const e = shallowReactive([])
let eb = shallowReactive(null)
const l = shallowReactive([])
let lb = shallowReactive(null)
const o = shallowReactive([])
let ob = shallowReactive(null)
const d = shallowReactive([])
let db = shallowReactive(null)

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
  data: ['材料', '机械设备', '人工', '其他', '间接支出'],
  textStyle: {fontSize: 14, color: '#000'},
  itemWidth: 25,
  itemHeight: 15,
  itemGap: 15,
  right: '10%',
};
const grid = {top: '5%', left: '10%', right: '6%', bottom: '25%'};

const currentInstance = getCurrentInstance();
const {$http} = currentInstance.appContext.config.globalProperties;

$http.get('/getSumByDateAndType?pid=' + props.pid).then((res) => {
  for (let i = 0; i < res.data["材料"].length; i++) {
    m.push([res.data["材料"][i]["date"], res.data["材料"][i]["sum"]])
  }
  for (let i = 0; i < res.data["机械设备"].length; i++) {
    e.push([res.data["机械设备"][i]["date"], res.data["机械设备"][i]["sum"]])
  }
  for (let i = 0; i < res.data["人工"].length; i++) {
    l.push([res.data["人工"][i]["date"], res.data["人工"][i]["sum"]])
  }
  for (let i = 0; i < res.data["其他"].length; i++) {
    o.push([res.data["其他"][i]["date"], res.data["其他"][i]["sum"]])
  }
  for (let i = 0; i < res.data["间接支出"].length; i++) {
    d.push([res.data["间接支出"][i]["date"], res.data["间接支出"][i]["sum"]])
  }
  $http.get("/findConstByPid?pid=" + props.pid).then((res) => {
    // xAxis
    const xAxis = {
      type: 'time',
      //min: "'"+res.data.estart+"'",//最小值为2022-04-10
      //min:'2022-04-15',
      axisLine: {lineStyle: {color: '#C7DEFF'}},
      splitLine: {lineStyle: {color: '#C7DEFF'}},
      axisLabel: {
        rotate: 40,
        textStyle: {fontSize: 12, color: '#2B6BD1'},
        //formatter: '{yyyy}-{MM}-{dd}'
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
      //data: dataArr.xdata,
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
      mb = res.data.mbudget
      eb = res.data.ebudget
      lb = res.data.lbudget
      ob = res.data.obudget
      db = res.data.ibudget
      console.log(mb, eb, lb, ob, db)
      // series
      const series = [
        {
          name: '材料',
          type: 'line',
          smooth: true, //是否平滑
          showAllSymbol: true,
          // symbol: 'image://./static/images/guang-circle.png',
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
                "yAxis": mb
              }
            ],
            "label": {
              "show": true,
              "normal": {
                "formatter": "材料预算",

              }
            }
          },
          data: m,
        },

        {
          name: '机械设备',
          type: 'line',
          smooth: true, //是否平滑
          showAllSymbol: true,
          // symbol: 'image://./static/images/guang-circle.png',
          symbol: 'circle',
          symbolSize: 10,
          lineStyle: {
            normal: {
              color: '#FEEEB2',
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
              color: '#FEEEB2',
            },
          },

          itemStyle: {
            color: '#FEEEB2',
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
                "color": "#FEEEB2"
              }
            },
            "data": [
              {
                "yAxis": eb
              }
            ],
            "label": {
              "show": true,
              "normal": {
                "formatter": "机械设备预算",

              }
            }
          },
          data: e
        },

        {
          name: '人工',
          type: 'line',
          smooth: true, //是否平滑
          showAllSymbol: true,
          // symbol: 'image://./static/images/guang-circle.png',
          symbol: 'circle',
          symbolSize: 10,
          lineStyle: {
            normal: {
              color: '#CBC0FF',
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
              color: '#CBC0FF',
            },
          },

          itemStyle: {
            color: '#CBC0FF',
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
                "color": "#CBC0FF"
              }
            },
            "data": [
              {
                "yAxis": lb
              }
            ],
            "label": {
              "show": true,
              "normal": {
                "formatter": "人工预算",

              }
            }
          },
          data: l
        },

        {
          name: '其他',
          type: 'line',
          smooth: true, //是否平滑
          showAllSymbol: true,
          // symbol: 'image://./static/images/guang-circle.png',
          symbol: 'circle',
          symbolSize: 10,
          lineStyle: {
            normal: {
              color: '#B5D5FF',
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
              color: '#B5D5FF',
            },
          },

          itemStyle: {
            color: "#B5D5FF",
            borderColor: "#fff",
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
                "color": "#B5D5FF"
              }
            },
            "data": [
              {
                "yAxis": ob
              }
            ],
            "label": {
              "show": true,
              "normal": {
                "formatter": "其他预算",

              }
            }
          },
          data: o
        },

        {
          name: '间接支出',
          type: 'line',
          smooth: true, //是否平滑
          showAllSymbol: true,
          // symbol: 'image://./static/images/guang-circle.png',
          symbol: 'circle',
          symbolSize: 10,
          lineStyle: {
            normal: {
              color: '#FA9898',
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
              color: '#FA9898',
            },
          },

          itemStyle: {
            color: '#FA9898',
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
                "color": "#FA9898"
              }
            },
            "data": [
              {
                "yAxis": db
              }
            ],
            "label": {
              "show": true,
              "normal": {
                "formatter": "间接支出预算",

              }
            }
          },
          data: d
        },
      ];
      option = {tooltip, xAxis, yAxis, series, grid, legend, backgroundColor: '#fff'};

      myChart = echarts.init(document.getElementById(p));
      myChart.setOption(option);
      window.addEventListener("resize", function () {
        myChart.resize();
      })
    })
  })
})
</script>

<style scoped>

</style>