<template>
  <div id="myChart5"
       :style="{width: '1000px', height: '600px',margin:'10px'}"></div>
</template>

<script>
import * as echarts from "echarts";
import {getCurrentInstance} from "vue";
import moment from "moment";

export default {
  name: "progresschart",
  setup() {
    var categories = [];
    var dayTime = 3600 * 24 * 1000;

    var nowDate = new Date();//今日日期
    var nowDateStr = formatDate(nowDate);
    console.log(nowDateStr);

    function formatDate(date) {
      //将2020/2/3这种日期格式，转换为2020-02-03
      var arr = date.toLocaleDateString().split("/");
      if (arr[1].length < 2) {//对月份进行处理：如果月份的长度为1，则拼接上字符串0
        arr.splice(1, 1, "0" + arr[1]);
      }
      if (arr[2].length < 2) {//对天数进行处理：如果天数的长度为2，则拼接上字符串0
        arr.splice(2, 1, "0" + arr[2]);
      }
      return arr.join("-");
    }

    var data = [];

    //在渲染时，data中的每个数据项都会调用这个方法
    function renderItem(params, api) {
      //params为data中的数据项的信息对象    api中是一些开发者可调用的方法集合，可以对data中的数据项进行操作
      var categoryIndex = api.value(0);//取出data中数据项的第一个维度的值

      //===============计划工期进度条
      //计划开始日期(在屏幕上的像素值)
      var planStartDate = api.coord([api.value(1), categoryIndex]);//将数据项中的数值对应的坐标系上的点，转换为屏幕上的像素值
      //坐标系上的点：是数据项映射到坐标系的x轴和y轴后，对应的位置
      //屏幕上的像素值：是坐标系上的点，在屏幕上的位置
      //计划结束日期(在屏幕上的像素值)
      var planEndDate = api.coord([api.value(2), categoryIndex]);
      //由于data.value中维度1和维度2的数据会被映射到x轴，而x轴的type为time，即时间轴，
      //所以api.value(1)和api.value(2)获取到的值是将日期转换后的毫秒值
      //设置图形的高度
      var height = api.size([0, 1])[1] * 0.4;//获得Y轴上数值范围为1的一段所对应的像素长度；这是官方文档的注释，对于api.size()方法，目前我还不是很理解；先做个标记??? 以后再说

      //使用graphic图形元素组件，绘制矩形
      //clipRectByRect方法，在绘制矩形时，如果矩形大小超出了当前坐标系的包围盒，则裁剪这个矩形
      var rectShape1 = echarts.graphic.clipRectByRect({
        //矩形的位置
        x: planStartDate[0],
        y: planStartDate[1],
        //矩形的宽高
        width: planEndDate[0] - planStartDate[0],
        height: height
      }, {
        //当前坐标系的包围盒
        x: params.coordSys.x,
        y: params.coordSys.y,
        width: params.coordSys.width,
        height: params.coordSys.height
      });


      //===============实际工期进度条
      var rectShape2 = null;
      //判断实际开始日期是否为空，如果为空，说明项目还没开始
      if (api.value(3) !== '') {//如果实际开始日期不为空
        //实际开始日期(在屏幕上的像素值)
        var practiceStartDate = api.coord([api.value(3), categoryIndex]);
        console.log(practiceStartDate)
        //当前日期或实际结束日期
        var nowDate_or_practiceEndDate;
        //项目周期(毫秒值)：计划结束日期(毫秒值) - 计划开始日期(毫秒值)
        var projectCycle_millisecond = api.value(2) - api.value(1);
        //实际开始日期(毫秒值)
        var practiceStartDate_millisecond = +echarts.number.parseDate(api.value(3));
        //当前日期(毫秒值)
        var nowDate_millisecond = +echarts.number.parseDate(nowDateStr);
        if (!api.value(4)) {
          //项目未完成
          //取今天
          var today=moment().format("YYYY-MM-DD");
          nowDate_or_practiceEndDate=api.coord([today,categoryIndex])
        } else {
          //项目已完成
          //取实际结束日期(在屏幕上的像素值)
          nowDate_or_practiceEndDate=api.coord([api.value(4),categoryIndex])
        }

        //使用graphic图形元素组件，绘制矩形
        //clipRectByRect方法，在绘制矩形时，如果矩形大小超出了当前坐标系的包围盒，则裁剪这个矩形
        rectShape2 = echarts.graphic.clipRectByRect({
          //矩形的位置
          x: practiceStartDate[0],
          y: practiceStartDate[1],
          //矩形的宽高
          width: nowDate_or_practiceEndDate[0] - practiceStartDate[0],
          height: height
        }, {
          //当前坐标系的包围盒
          x: params.coordSys.x,
          y: params.coordSys.y,
          width: params.coordSys.width,
          height: params.coordSys.height
        });
      }

      //如果项目还没开始，那么只渲染计划工期的进度条
      if (rectShape2 == null) {
        //设置绘制的矩形的元素定义
        return rectShape1 && {
          type: 'group',
          children: [
            {
              //类型为矩形
              type: 'rect',
              //具体形状
              shape: rectShape1,
              //样式
              style: api.style({
                fill: '#AACCF9'
              })
            }
          ]
        };
      } else {//渲染计划工期和实际工期
        //设置绘制的矩形的元素定义
        return rectShape1 && rectShape2 && {
          type: 'group',
          children: [
            {
              //类型为矩形
              type: 'rect',
              //具体形状
              shape: rectShape1,
              //样式
              style: api.style({
                fill: '#AACCF9'
              })
            },
            {
              //类型为矩形
              type: 'rect',
              //具体形状
              shape: rectShape2,
              //样式
              style: api.style({
                fill: '#2076ED'
              })
            }
          ]
        };
      }
    }

    let option;

    const currentInstance = getCurrentInstance();
    const {$http} = currentInstance.appContext.config.globalProperties;

    $http.get("/getconstbeans").then((res) => {
      for(let i=0;i<res.data.length;i++){
        categories.push(res.data[i].pname);
        data.push({name: res.data[i].pname,value: [i,res.data[i].estart,res.data[i].eend,res.data[i].start,res.data[i].end]})
      }
      option = {
        tooltip: {
          //自定义提示信息
          formatter: function (params) {//params为当前点击图形元素的数据信息的对象
            //计划开始时间
            var planStartDate = params.value[1];
            //计划结束时间
            var planEndDate = params.value[2];
            //实际开始时间
            var practiceStartDate = "";
            var practiceStartDate_str = "";
            if (params.value[3]) {
              practiceStartDate = params.value[3];
              practiceStartDate_str = '实际开始日期：' + practiceStartDate + '<br/>';
            }

            var practiceEndDate = "";
            var practiceEndDate_str = "";
            if(params.value[4]){
              practiceEndDate=params.value[4];
              practiceEndDate_str='实际结束日期：'+practiceEndDate+'<br/>';
            }else{
              practiceEndDate=moment().format("YYYY-MM-DD");
              practiceEndDate_str='正在建设中--今日是：'+practiceEndDate+'<br/>';
            }

            //项目周期(毫秒值)：计划结束日期 - 计划开始日期
            var projectCycle_millisecond = +echarts.number.parseDate(params.value[2]) - +echarts.number.parseDate(params.value[1]);
            //项目周期(天数)
            var projectCycle_days = projectCycle_millisecond / dayTime;
            //当前进度(百分比)
            var currentProgress_percentage;
            var currentProgress_percentage_str = "";
            if (practiceStartDate !== '') {//如果实际开始日期不为空，说明项目已开始
              //当前进度(毫秒值)：当前日期(毫秒值) - 实际开始日期(毫秒值)
              var currentProgress_mill = +echarts.number.parseDate(nowDateStr) - +echarts.number.parseDate(params.value[3]);
              //当前进度(百分比)：当前进度(毫秒值) / 项目周期(毫秒值)
              currentProgress_percentage = ((currentProgress_mill / projectCycle_millisecond) * 100).toFixed(0);//注意，toFixed的返回值是字符串类型
              //如果项目已结束：比如计划开始时间1月10日，计划结束时间1月20日，项目周期10天，实际开始时间1月10日，当前日期1月22日，说明项目已结束
              //那么按照(当前日期-实际开始日期)/项目周期，计算出的百分比，就会大于100，所以需要将百分比置为100
              if (currentProgress_percentage > 100) {//项目已结束
                currentProgress_percentage = 100;
              }
              currentProgress_percentage_str = '当前进度：' + currentProgress_percentage + '%' + '<br/>';
            }

            return params.name + '<br/>'
                + '计划开始时间：' + planStartDate + '<br/>'
                + '计划结束时间：' + planEndDate + '<br/>'
                + practiceStartDate_str
                + practiceEndDate_str
          }
        },
        title: {
          text: '项目进度',
          left: 'center'
        },

        //图表底板
        grid: {
          x:80,
          height: 300,
          width:800
        },
        xAxis: {
          type: "time",//x轴类型为时间轴
          //min: 1646064000000,//最小值为2022-04-10
          //max: 1672416000000,//最大值为2022-12-31
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
        },
        yAxis: {
          data: categories,
          interval: 'auto',
          axisTick: {
            alignWithLabel: true//保证刻度线和标签对齐，当boundaryGap为true的时候有效，不过boundaryGap默认就是true
          }
        },
        legend: {
          left: '70%',
          top: 10,
          data: ['计划工期', '实际工期']
        },
        series: [
          {
            type: 'custom',
            //使用自定义的图形元素
            render: renderItem,
            renderItem: renderItem,
            name: '计划工期',
            itemStyle: {
              //透明度
              opacity: 0.8,
              color: '#AACCF9'
            },
            encode: {
              //将维度1和维度2的数据映射到x轴
              x: [1, 2],
              //将维度0的数据映射到y轴
              y: 0
            },
            data: data
          },
          //这个系列并没有太大作用，也没有给它设置data，只是为了通过这个系列，显示图例(legend)而已
          {
            type: 'custom',
            name: '实际工期',
            itemStyle: {
              //透明度
              opacity: 0.8,
              color: '#2076ED'
            }
          }
        ]
      };
      let myChart5 = echarts.init(document.getElementById("myChart5"));
      myChart5.setOption(option);
      myChart5.resize();
    })
  }
}
</script>

<style scoped>

</style>