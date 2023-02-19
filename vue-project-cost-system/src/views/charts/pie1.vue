<template>
  <div id="myChart1"
       :style="{width: '200px', height: '200px',margin:'20px'}"></div>
</template>

<script>
import {getCurrentInstance, onMounted} from "vue";
import * as echarts from "echarts";

export default {
  name: "pie1",
  setup() {
    const currentInstance = getCurrentInstance()
    const {$http} = currentInstance.appContext.config.globalProperties

    let data1 = []
    data1.push({value: 9897, name: "沣东新城"})

    onMounted(() => {//需要获取到element,所以是onMounted的Hook
      const colorList = [
        "#02F2F3",
        "#68B5FB",
        "#D45A49",
        "#F3E58D",
        "#C0FE5D",
        "#15DE57",
        "#F9F936"
      ];

      const option1 = {
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        legend: {
          orient: "vertical",
          bottom: "25%",
          right: "5%",
          textStyle: {
            fontSize: 14,
            color: "black"
          },
          formatter: function (name) {
            const item = data1.filter((item) => item.name === name)[0];
            return `${name}：${item.value}人`;
          }
        },
        series: [
          {
            type: "pie",
            center: ["30%", "50%"],
            radius: ["60%", "80%"],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: "center"
            },
            itemStyle: {
              normal: {
                color: function (params) {
                  return colorList[params.dataIndex];
                }
              }
            },
            emphasis: {
              label: {
                show: true,
                position: "outside",
                fontSize: "20",
                formatter: function (params) {
                  var percent = 0;
                  var total = 0;
                  for (var i = 0; i < data1.length; i++) {
                    total += data1[i].value;
                  }
                  percent = (
                      (params.value / total) *
                      100
                  ).toFixed(0);
                  if (params.name !== "") {
                    return (
                        params.name +
                        "\n" +
                        params.value +
                        "人" +
                        "\n" +
                        "占百分比：" +
                        percent +
                        "%"
                    );
                  } else {
                    return "";
                  }
                }
              }
            },
            labelLine: {
              show: false
            },
            data: data1
          }
        ]
      };

      $http.get("/test").then(res => {
        data1.push({value: res.data[0].value, name: res.data[0].name})
        data1.push({value: 1230, name: "其他区县"})
        // 绘制图表
        let myChart1 = echarts.init(document.getElementById("myChart1"));
        myChart1.setOption(option1);
        myChart1.resize();
      })
      window.onresize = function () {//自适应大小
      };
    });
  }
}
</script>

<style scoped>

</style>