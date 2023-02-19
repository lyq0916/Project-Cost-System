<template>
  <el-row>
    <ConstBasic :pid=id style="width: 38.5%"></ConstBasic>
    <liquidfill :pid=id style="width: 50%;margin-left: 40px"></liquidfill>
  </el-row>
  <el-row>
      <costpie1 :pid=id style="width: 45%"></costpie1>
      <budgetpie :pid=id style="width: 50%;margin-left: 40px"></budgetpie>
  </el-row>
  <el-row>
    <el-col>
      <linechart :pid=id></linechart>
    </el-col>
    <el-col>
      <comparebar :pid=id></comparebar>
    </el-col>
  </el-row>
</template>

<script lang="ts" setup>
import {getCurrentInstance, ref, shallowReactive} from "vue";
import Costpie1 from "../charts/costpie1.vue";
import Budgetpie from "../charts/budgetpie.vue";
import Linechart from "../charts/linechart.vue";
import Comparebar from "../charts/comparebar.vue";
import Liquidfill from "../charts/liquidfill.vue";
import ConstBasic from "./ConstBasic.vue";

const props = defineProps({
  pid: Number,
})
const id = props.pid;
</script>

<style scoped>

</style>