<template>
  <el-card>
    <template #header>
      <div class="clearfix">
        <span>成本支出</span>
      </div>
    </template>
    <div :id="p"
         :style="{width: '400px', height: '300px',margin:'10px'}"
         ref="chart"
    ></div>
  </el-card>
  <el-dialog v-model="dialogFormVisible" title="支出明细">
    <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="记录编号"/>
      <el-table-column prop="name" label="名称"/>
      <el-table-column prop="type" label="支出类型"/>
      <el-table-column prop="money" label="支出金额(万元)"/>
      <el-table-column prop="pid" label="项目编号"/>
      <el-table-column prop="note" label="备注"/>
      <el-table-column type="date" prop="date" label="记录日期"/>
    </el-table>
  </el-dialog>
</template>

<script>
import {getCurrentInstance, inject, reactive, ref, shallowReactive} from "vue";
import * as echarts from "echarts";

export default {
  name: "costpie1",
  props: ['pid'],
  setup(props, context) {
    const currentInstance = getCurrentInstance()
    const {$http} = currentInstance.appContext.config.globalProperties

    //点击饼图弹出对话框
    let dialogFormVisible = ref(false)
    const tableData = shallowReactive([]);
    const loading =ref(false)
    //饼图相关
    let p = ref(null);
    p = Math.round(Math.random() * 100)
    const chart = ref(null);
    let bgColor = '#fff';
    let title = '总支出';
    let color = [
      {
        c1: '#FD866A',
        c2: '#EE6666',
      }, {
        c1: '#73DDFF',
        c2: '#8C9FFE',
      }, {
        c1: '#1DB8B0',
        c2: '#32CCC4',
      }, {
        c1: '#1EC15A',
        c2: '#3BA272',
      }, {
        c1: '#B1B632',
        c2: '#BFC444',
      }, {
        c1: '#499AF2',
        c2: '#67B0FF',
      }, {
        c1: '#1DB8B0',
        c2: '#32CCC4',
      }, {
        c1: '#FD866A',
        c2: '#EE6666',
      },
    ];
    let echartData = [];

    let formatNumber = function (num) {
      let reg = /(?=(\B)(\d{3})+$)/g;
      return num.toString().replace(reg, ',');
    }

    function accAdd(arg1, arg2) {
      if (isNaN(arg1)) {
        arg1 = 0;
      }
      if (isNaN(arg2)) {
        arg2 = 0;
      }
      arg1 = Number(arg1);
      arg2 = Number(arg2);
      var r1, r2, m, c;
      try {
        r1 = arg1.toString().split(".")[1].length;
      } catch (e) {
        r1 = 0;
      }
      try {
        r2 = arg2.toString().split(".")[1].length;
      } catch (e) {
        r2 = 0;
      }
      c = Math.abs(r1 - r2);
      m = Math.pow(10, Math.max(r1, r2));
      if (c > 0) {
        var cm = Math.pow(10, c);
        if (r1 > r2) {
          arg1 = Number(arg1.toString().replace(".", ""));
          arg2 = Number(arg2.toString().replace(".", "")) * cm;
        } else {
          arg1 = Number(arg1.toString().replace(".", "")) * cm;
          arg2 = Number(arg2.toString().replace(".", ""));
        }
      } else {
        arg1 = Number(arg1.toString().replace(".", ""));
        arg2 = Number(arg2.toString().replace(".", ""));
      }
      return (arg1 + arg2) / m;
    }

    let total;
    let option;
    let myChart1;
    const reload = inject('reload');

    function show(pid) {
      $http.get('/costtypebypid?pid=' + pid, {}).then(res => {
        for (let i = 0; i < res.data.length; i++) {
          echartData.push({value: res.data[i][1], name: res.data[i][0]});
        }
        total = echartData.reduce((a, b) => {
          return accAdd(a, b.value)
        }, 0);
        //console.log(total);
        option = {
          backgroundColor: bgColor,
          color: 'red',
          title: [{
            text: '{val|' + formatNumber(total) + '}\n{name|' + title + '}',
            top: 'center',
            left: 'center',
            textStyle: {
              rich: {
                name: {
                  fontSize: 14,
                  fontWeight: 'normal',
                  color: '#666666',
                  padding: [10, 0]
                },
                val: {
                  fontSize: 20,
                  fontWeight: 'bold',
                  color: '#333333',
                }
              }
            }
          }, {
            text: '单位：万元',
            bottom: 20,
            right: 20,
            textStyle: {
              fontSize: 14,
              color: '#666666',
              fontWeight: 400
            }
          }],
          series: [{
            type: 'pie',
            radius: ['50%', '30%'],
            center: ['50%', '50%'],
            data: echartData,
            hoverAnimation: false,
            itemStyle: {
              normal: {
                borderColor: bgColor,
                borderWidth: 2,
                color: params => {
                  return {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [{
                      offset: 0,
                      color: color[params.dataIndex].c2 // 0% 处的颜色
                    }, {
                      offset: 1,
                      color: color[params.dataIndex].c1 // 100% 处的颜色
                    }],
                    global: false // 缺省为 false
                  }
                },
              }
            },
            labelLine: {
              normal: {
                length: 50,
                lineStyle: {
                  width: 1
                }
              }
            },
            label: {
              normal: {
                formatter: params => {
                  return (
                      '{name|' + params.name + '}\n' + '{value|' + params.value + '}万元'
                  );
                },
                color: 'black', //label的颜色
                lineHeight: '20',
                rich: {
                  icon: {
                    fontSize: 16,
                    color: 'auto' //icon的颜色
                  },
                  name: {
                    fontSize: 14,
                    padding: [0, 10, 0, 4],
                    color: '#666666'  //name的颜色
                  }
                }
              }
            },
          }]
        };
        if (myChart1 == null) {
          myChart1 = echarts.init(document.getElementById(p));
        }
        myChart1.setOption(option);
        myChart1.resize();
        myChart1.on('click', function (params) {
          //console.log(params.name)
          loading.value=true
          $http.get('/findByPidAndType?pid=' + props.pid + '&type=' + params.name).then(res => {
            tableData.splice(0, tableData.length);
            for (let i = 0; i < res.data.length; i++) {
              tableData.push({
                id: res.data[i].mid,
                name: res.data[i].name,
                type: res.data[i].type,
                money: res.data[i].money,
                pid: res.data[i].pid,
                note: res.data[i].note,
                date: res.data[i].updatedate
              })
            }
          })
          loading.value=false
          dialogFormVisible.value = true
        },)

      })
    }
    let newPromise = new Promise((resolve) => {
      resolve()
    })
    //然后异步执行echarts的初始化函数
    newPromise.then(() => {
      //console.log(props.pid)
      show(props.pid)
    })
    return {
      loading,
      chart,
      p,
      dialogFormVisible,
      tableData,
    }
  },

}
</script>

<style scoped>

</style>