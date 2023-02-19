<template>
  <div id="myChart2"
       :style="{width: '350px', height: '300px',margin:'10px'}"></div>
</template>

<script>
import {getCurrentInstance, onMounted} from "vue";
import * as echarts from "echarts";

export default {
  name: "pie",
  setup() {
    const dataList = []
    const handleData = {};
    let sum = 0;
// 求和
    dataList.map((item) => {
      sum += item.value;
    });
// 数据处理
    dataList.map((item) => {
      handleData[item.name] = item;
      handleData[item.name].percentage = ((item.value / sum) * 100).toFixed(1) + '%';
    });

    let option;
    option = {
      legend: {
        show: true,
        icon: 'circle',
        right: '3%',
        left: 'center',
        bottom: '10%',
        itemWidth: 10,
        itemStyle: {
          borderColor: 'none',
        },
        textStyle: {
          rich: {
            name: {
              color: '#606266',
              fontSize: 14,
            },
            percentage: {
              color: '#606266',
              fontSize: 16,
              padding: [0, 10, 0, 30],
              align: 'right',
            },
          },
        },
      },
      tooltip: {
        trigger: 'item',
      },
      series: [
        {
          type: 'pie',
          color: ['#73DDFF', '#73ACFF', '#FDD56A', '#FDB36A',
            '#FD866A', '#9E87FF', '#58D5FF', '#EE6666',
            '#F5A8F6', '#FA9898', '#b0c4de',
            '#3BA272', '#FC8452', '#F3AA4D'],
          radius: '70%',
          left: 'center',
          data: dataList,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)',
            },
          },
          labelLine: {
            show: true,
            length: 10,
            length2: 30,
          },
          label: {
            width: 100,
            fontSize: 12,
          },
          itemStyle: {
            borderColor: '#fff',
          },
        },
      ],
    };

    const currentInstance = getCurrentInstance()
    const {$http} = currentInstance.appContext.config.globalProperties

    $http.get('/groupbytype').then(res=>{
      //console.log(res.data[0][0]);
      for(var i=0;i<res.data.length;i++){
        dataList.push({value:res.data[i][1],name:res.data[i][0]})
      }
      let myChart2 = echarts.init(document.getElementById("myChart2"));
      myChart2.setOption(option);
      myChart2.resize();
    })

    /*echarts异步问题  document还未创建出来*/
    // 新建一个promise对象
   /* let newPromise = new Promise((resolve) => {
      resolve()
    })
    //然后异步执行echarts的初始化函数
    newPromise.then(() => {
      let myChart2 = echarts.init(document.getElementById("myChart2"));
      myChart2.setOption(option);
      myChart2.resize();
    })*/
  }
}
</script>

<style scoped>

</style>