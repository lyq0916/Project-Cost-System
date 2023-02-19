<template>
  <el-form :model="form">
    <el-form-item>
      <el-col>
        <el-row>
          <el-input v-model="form.pname" placeholder="输入项目名称" suffix-icon="icon-mya-Group46"
                    style="width: 300px;height:30px;margin: 10px"></el-input>
          <el-button type="primary" @click="search" style="height: 30px ;margin: 10px">搜索</el-button>
        </el-row>
        <el-row>
          <el-input v-model="form.pid" placeholder="输入项目编号" suffix-icon="icon-mya-Group46"
                    style="width: 300px;height:30px;margin: 10px"></el-input>
          <el-button type="primary" @click="search1" style="height: 30px ;margin: 10px">搜索</el-button>
        </el-row>
      </el-col>
    </el-form-item>
  </el-form>
  <el-card>
    <template #header>
      <div class="clearfix">
        <span>展示项目: {{ pname }}</span>
      </div>
    </template>
    <div id="myChart1"
         :style="{width: '500px', height: '300px',margin:'10px'}"></div>
  </el-card>
</template>

<script>
import {getCurrentInstance, inject, reactive, ref} from "vue";
import * as echarts from "echarts";

export default {
  name: "costpie",
  setup() {
    const currentInstance = getCurrentInstance()
    const {$http} = currentInstance.appContext.config.globalProperties

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
    let pname = ref(null);
    const form = reactive({
      pname: '',
      pid:'',
    })
    const reload = inject('reload');
    let pid = 1;
    //show(pid);
    function show(pid) {
      $http.get('/costtypebypid?pid=' + pid, {}).then(res => {
        for (let i = 0; i < res.data.length; i++) {
          echartData.push({value: res.data[i][1], name: res.data[i][0]});
        }
        total = echartData.reduce((a, b) => {
          return accAdd(a, b.value)
        }, 0);
        console.log(total);
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
          myChart1 = echarts.init(document.getElementById("myChart1"));
        }
        myChart1.setOption(option);
        myChart1.on('click',function (params)
        {

          console.log(params.data.name);
        })
        myChart1.resize();
      })
    }

    function search() {
      pname = form.pname;
      //console.log(form.pname);
      $http.get('/searchproject?pname=' + form.pname, {}).then(response => {
        pid = response.data[0].pid;
        show(pid);
        //reload();
      })
    }
    function search1(){
      show(form.pid)
    }

    return {
      form,
      pname,
      search,
      search1,
    }
  }
}
</script>

<style scoped>

</style>