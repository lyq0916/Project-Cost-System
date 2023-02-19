<template>
  <div class="container">
    <el-row gutter=15>
      <el-col span=12>
        <el-card shadow="hover" class="m">
          <template #header>
            <div class="clearfix">
              <span>今日数据统计</span>
            </div>
          </template>
          <costpie ref="costpie"></costpie>
        </el-card>
        <el-card shadow="hover" class="m">
          <!--根据项目类型画出饼图-->
          <template #header>
            <div class="clearfix">
              <span>项目类型分类</span>
            </div>
          </template>
          <pie ref="pie"></pie>
        </el-card>
      </el-col>
      <el-col span=15>
        <el-row :gutter="20" class="m">
          <el-col span=8>
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-1">
                <i class="icon-mya-Group18 grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ num.num1 }}</div>
                  <div>申报中</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col span=8>
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-2">
                <i class="icon-mya-Group14 grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ num.num2 }}</div>
                  <div>建设中</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col span=8>
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-3">
                <i class="icon-mya-Group31 grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ num.num3 }}</div>
                  <div>已完工</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-row>
          <el-col span=12>
            <el-card shadow="hover">
              <template #header>
                <div class="clearfix">
                  <span>项目分布地点</span>
                </div>
              </template>
              <chinamap ref="chinamap"></chinamap>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-row gutter=20>
      <el-col span=15>
        <el-card shadow="hover" class="m">
          <template #header>
            <div class="clearfix">
              <span>项目进度总览</span>
            </div>
          </template>
          <progresschart ref="progresschart"></progresschart>
        </el-card>
      </el-col>
    </el-row>
    <el-row gutter=20>
      <el-col span=15>
        <el-card shadow="hover" class="m">
          <template #header>
            <div class="clearfix">
              <span>项目间成本比较柱状图</span>
            </div>
          </template>
          <costbar ref="costbar"></costbar>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col>
        <el-card>
          <template #header>
            <div class="clearfix">
              <span>投标中项目</span>
            </div>
          </template>
          <el-table :data="tableData1" style="width: 100%">
            <el-table-column label="项目编号" width="150">
              <template #default="scope1">
                <div style="display: flex; align-items: center">
                  <span style="margin-left: 10px">{{ scope1.row.pid }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="数据看板" width="150">
              <template #default="scope1">
                <el-button size="small" @click="dataShow1(scope1.$index, scope1.row)"
                >查看
                </el-button>
              </template>
            </el-table-column>
            <el-table-column label="项目名称">
              <template #default="scope1">
                <span style="margin-left: 10px">{{ scope1.row.pname }}</span>
              </template>
            </el-table-column>
            <el-table-column label="项目状态">
              <template #default="scope1">
                <div style="display: flex; align-items: center">
                  <span style="margin-left: 10px">{{ scope1.row.state }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="项目详情">
              <template #default="scope1">
                <el-button size="small" @click="handleShow1(scope1.$index, scope1.row)"
                >查看
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-card>
        <template #header>
          <div class="clearfix">
            <span>在建项目</span>
          </div>
        </template>
        <el-table :data="tableData2" style="width: 100%">
          <el-table-column label="项目编号" width="150">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <span style="margin-left: 10px">{{ scope.row.pid }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="数据看板" width="150">
            <template #default="scope">
              <el-button size="small" @click="dataShow2(scope.$index, scope.row)"
              >查看
              </el-button>
            </template>
          </el-table-column>
          <el-table-column label="项目名称" width="150">
            <template #default="scope">
              <el-popover effect="light" trigger="hover" placement="top" width="auto">
                <template #default>
                  <div>项目编号: {{ scope.row.pid }}</div>
                  <div>项目名称: {{ scope.row.pname }}</div>
                </template>
                <template #reference>
                  <span style="margin-left: 10px">{{ scope.row.pname }}</span>
                </template>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column label="项目状态" width="150">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <span style="margin-left: 10px">{{ scope.row.state }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="项目详情" width="150">
            <template #default="scope">
              <el-button size="small" @click="handleShow2(scope.$index, scope.row)"
              >查看
              </el-button>
            </template>
          </el-table-column>
          <el-table-column label="预算(万元)" width="150">
            <template #default="scope">
              <span style="margin-left: 10px">{{ scope.row.budget }}</span>
            </template>
          </el-table-column>
<!--          <el-table-column label="已花成本" width="150">
            <template #default="scope">
              <span style="margin-left: 10px">{{ scope.row.cost }}</span>
            </template>
          </el-table-column>-->
        </el-table>
      </el-card>
    </el-row>
    <el-row>
      <el-card>
        <template #header>
          <div class="clearfix">
            <span>完工项目</span>
          </div>
        </template>
        <el-table :data="tableData3" style="width: 100%">
          <el-table-column label="项目编号" width="150">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <span style="margin-left: 10px">{{ scope.row.pid }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="数据看板" width="150">
            <template #default="scope">
              <el-button size="small" @click="dataShow3(scope.$index, scope.row)"
              >查看
              </el-button>
            </template>
          </el-table-column>
          <el-table-column label="项目名称" width="150">
            <template #default="scope">
              <el-popover effect="light" trigger="hover" placement="top" width="auto">
                <template #default>
                  <div>项目编号: {{ scope.row.pid }}</div>
                  <div>项目名称: {{ scope.row.pname }}</div>
                </template>
                <template #reference>
                  <span style="margin-left: 10px">{{ scope.row.pname }}</span>
                </template>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column label="项目状态" width="150">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <span style="margin-left: 10px">{{ scope.row.state }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="项目详情" width="150">
            <template #default="scope">
              <el-button size="small" @click="handleShow2(scope.$index, scope.row)"
              >查看
              </el-button>
            </template>
          </el-table-column>
          <el-table-column label="预算(万元)" width="150">
            <template #default="scope">
              <span style="margin-left: 10px">{{ scope.row.budget }}</span>
            </template>
          </el-table-column>
          <el-table-column label="已花成本" width="150">
            <template #default="scope">
              <span style="margin-left: 10px">{{ scope.row.cost }}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </el-row>
    <!--申报中项目数据展示-->
    <el-dialog v-model="visible1" :title="d.pname" width="80%">
      <el-card>
        <el-steps :space="200" :active="0" finish-status="success">
          <el-step title="申报中"/>
          <el-step title="建设中"/>
          <el-step title="已完工"/>
        </el-steps>
      </el-card>
      <BidDialog :key="d.id" :pid=d.id></BidDialog>
    </el-dialog>
    <el-dialog v-model="p1" title="项目详情">
      <el-table :data="project1" border stripe style="width: 100%">
        <el-table-column prop="id" label="项目编号"/>
        <el-table-column prop="name" label="项目名称"/>
        <el-table-column prop="bnumber" label="招标编号"/>
        <el-table-column prop="state" label="状态"/>
        <el-table-column prop="address" label="地址"/>
        <el-table-column prop="type" label="类型"/>
        <el-table-column prop="budget" label="预算(万元)"/>
        <el-table-column prop="date" label="报备日期"/>
      </el-table>
    </el-dialog>
    <!--在建项目数据展示-->
    <el-dialog v-model="visible2" :title="d.pname" width="80%">
      <el-card>
        <el-steps :space="200" :active="1" finish-status="success">
          <el-step title="申报中"/>
          <el-step title="建设中"/>
          <el-step title="已完工"/>
        </el-steps>
      </el-card>
      <ConstDialog :key="d.id" :pid=d.id></ConstDialog>
    </el-dialog>
    <el-dialog v-model="p2" title="项目详情">
      <el-table :data="project2" border stripe style="width: 100%">
        <el-table-column prop="id" label="项目编号"/>
        <el-table-column prop="name" label="项目名称"/>
        <el-table-column prop="bnumber" label="招标编号"/>
        <el-table-column prop="state" label="状态"/>
        <el-table-column prop="address" label="地址"/>
        <el-table-column prop="type" label="类型"/>
        <el-table-column prop="budget" label="预算(万元)"/>
        <el-table-column prop="date" label="报备日期"/>
      </el-table>
    </el-dialog>
    <!--已完工项目数据展示-->
    <el-dialog v-model="visible3" :title="d.pname" width="80%">
      <el-card>
        <el-steps :space="200" :active="3" finish-status="success">
          <el-step title="申报中"/>
          <el-step title="建设中"/>
          <el-step title="已完工"/>
        </el-steps>
      </el-card>
      <ConstDialog :key="d.id" :pid=d.id></ConstDialog>
    </el-dialog>
    <el-dialog v-model="p3" title="项目详情">
      <el-table :data="project3" border stripe style="width: 100%">
        <el-table-column prop="id" label="项目编号"/>
        <el-table-column prop="name" label="项目名称"/>
        <el-table-column prop="bnumber" label="招标编号"/>
        <el-table-column prop="state" label="状态"/>
        <el-table-column prop="address" label="地址"/>
        <el-table-column prop="type" label="类型"/>
        <el-table-column prop="budget" label="预算(万元)"/>
        <el-table-column prop="date" label="报备日期"/>
      </el-table>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import {getCurrentInstance, nextTick, reactive, ref, shallowReactive} from "vue";
import Pie from "./charts/typepie.vue";
import Costpie from "./charts/costpie.vue";
import Chinamap from "./charts/chinamap.vue";
import Progresschart from "./charts/progresschart.vue";
import Costbar from "./charts/costbar.vue";
import Costpie1 from "./charts/costpie1.vue";
import Linechart from "./charts/linechart.vue";
import Comparebar from "./charts/comparebar.vue";
import BidDialog from "./Dialog/BidDialog.vue";
import ConstDialog from "./Dialog/ConstDialog.vue";

export default {
  name: "Project",
  components: {ConstDialog, BidDialog, Comparebar, Linechart, Costpie1, Costbar, Progresschart, Chinamap, Costpie, Pie},
  setup() {

    const currentInstance = getCurrentInstance();
    const {$http} = currentInstance.appContext.config.globalProperties;

    const pie = ref(null)
    const costpie = ref(null)
    const line = ref(null)
    const chinamap = ref(null)
    const progresschart = ref(null)
    const costbar = ref(null)
    const visible = ref(false)
    /*dialog  展示的项目编号*/
    /*点击表格中不同行传递不同的项目编号给dialog中的组件*/
    const d = reactive({
      id: "",
      pname: ""
    })

    const tableData1 = shallowReactive([])
    const visible1 = ref(false)
    const p1 = ref(false)
    const project1 = shallowReactive([])

    const tableData2 = shallowReactive([])
    const visible2 = ref(false)
    const p2 = ref(false)
    const project2 = shallowReactive([])

    const tableData3 = shallowReactive([])
    const visible3 = ref(false)
    const p3 = ref(false)
    const project3 = shallowReactive([])

    function c() {
      visible.value = true;
    }

    let num1;
    let num2;
    let num3;
    const num = shallowReactive({
          num1,
          num2,
          num3,
        }
    )

    //折叠面板  各个项目成本情况
    const activeNames = ref(['1'])
    const handleChange = (val: string[]) => {
    }

    $http.get('/groupbystate').then((response) => {
      for (let i = 0; i < response.data.length; i++) {
        if (response.data[i][0] == '申报中') {
          num.num1 = response.data[i][1];
        } else if (response.data[i][0] == '建设中') {
          num.num2 = response.data[i][1];
        } else {
          num.num3 = response.data[i][1];
        }
      }
    })

    const data = shallowReactive([])
    $http.get('/projectshow').then(response => {
      for (let i = 0; i < response.data.length; i++) {
        data.push({
          id: response.data[i].pid,
          name: response.data[i].pname,
          bnumber: response.data[i].bnumber,
          state: response.data[i].state,
          address: response.data[i].address,
          type: response.data[i].type,
          budget: response.data[i].budget,
          mbudget: response.data[i].mbudget,
          ebudget: response.data[i].ebudget,
          lbudget: response.data[i].lbudget,
          obudget: response.data[i].obudget,
          ibudget: response.data[i].ibudget,
          date: response.data[i].date,
        })
      }
    })

    interface Project {
      pid: Number,
      pname: String,
      state: String,
    }

    //获取正在招标的项目信息
    $http.get('/getbidbeans1').then(res => {
      for (let i = 0; i < res.data.length; i++) {
        tableData1.push({
          pid: res.data[i].pid, pname: res.data[i].pname, state: res.data[i].state
        })
      }
    })

    const dataShow1 = (index: number, row: Project) => {
      d.id = String(row.pid)
      d.pname = row.pname
      visible1.value = true
    }

    const handleShow1 = (index: number, row: Project) => {
      p1.value = true;
      $http.get('/getprojectbyid?pid=' + row.pid, {}).then(response => {
        project1.splice(0, project1.length)
        project1.push({
          id: response.data.pid,
          name: response.data.pname,
          bnumber: response.data.bnumber,
          state: response.data.state,
          address: response.data.address,
          type: response.data.type,
          budget: response.data.budget,
          date: response.data.date,
        })
      })
    }

    //获取建设中项目信息
    $http.get('/getconstbeans1').then(res => {
      for (let i = 0; i < res.data.length; i++) {
        tableData2.push({
          pid: res.data[i].pid,
          pname: res.data[i].pname,
          state: res.data[i].state,
          budget: res.data[i].budget,
          cost: res.data[i].cost,
        })
      }
    })

    const dataShow2 = (index: number, row: Project) => {
      d.id = String(row.pid)
      d.pname = row.pname
      visible2.value = true
    }

    const handleShow2 = (index: number, row: Project) => {
      p2.value = true;
      $http.get('/getprojectbyid?pid=' + row.pid, {}).then(response => {
        project2.splice(0, project2.length)
        project2.push({
          id: response.data.pid,
          name: response.data.pname,
          bnumber: response.data.bnumber,
          state: response.data.state,
          address: response.data.address,
          type: response.data.type,
          budget: response.data.budget,
          date: response.data.date,
        })
      })
    }

    //获取已完工项目信息
    $http.get('/getcompletebeans').then(res => {
      for (let i = 0; i < res.data.length; i++) {
        tableData3.push({
          pid: res.data[i].pid,
          pname: res.data[i].pname,
          state: res.data[i].state,
          budget: res.data[i].budget,
          cost: res.data[i].cost,
        })
      }
    })

    const dataShow3 = (index: number, row: Project) => {
      d.id = String(row.pid)
      d.pname = row.pname
      visible3.value = true
    }

    const handleShow3 = (index: number, row: Project) => {
      p3.value = true;
      $http.get('/getprojectbyid?pid=' + row.pid, {}).then(response => {
        project3.splice(0, project3.length)
        project3.push({
          id: response.data.pid,
          name: response.data.pname,
          bnumber: response.data.bnumber,
          state: response.data.state,
          address: response.data.address,
          type: response.data.type,
          budget: response.data.budget,
          date: response.data.date,
        })
      })
    }

    return {
      d,
      pie,
      costpie,
      line,
      chinamap,
      progresschart,
      costbar,
      num,
      activeNames,
      handleChange,
      data,
      visible,
      c,
      tableData1,
      dataShow1,
      visible1,
      handleShow1,
      p1,
      project1,
      tableData2,
      dataShow2,
      visible2,
      handleShow2,
      p2,
      project2,
      tableData3,
      dataShow3,
      visible3,
      handleShow3,
      p3,
      project3,
    }
  },
  mounted() {
  },
}
</script>

<style>
.m {
  margin-bottom: 20px;
}

.grid-content {
  display: flex;
  align-items: center;
  height: 100px;
}

.grid-cont-right {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: #999;
  width: 105px;
}

.grid-num {
  font-size: 30px;
  font-weight: bold;
}

.grid-con-icon {
  font-size: 50px;
  width: 100px;
  height: 100px;
  text-align: center;
  line-height: 100px;
  color: #fff;
}

.grid-con-1 .grid-con-icon {
  background: rgb(45, 140, 240);
}

.grid-con-1 .grid-num {
  color: rgb(45, 140, 240);
}

.grid-con-2 .grid-con-icon {
  background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
  color: rgb(100, 213, 114);
}

.grid-con-3 .grid-con-icon {
  background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
  color: rgb(242, 94, 67);
}

.box {
  height: 800px;
  width: 1000px
}
</style>
