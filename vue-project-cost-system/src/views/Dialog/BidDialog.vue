<template>
  <el-row>
    <BasicCard :pid=id style="width: 45%"></BasicCard>
    <budgetpie :pid=id style="width: 50%;margin-left: 20px"></budgetpie>
  </el-row>
</template>

<script lang="ts" setup>
import {getCurrentInstance, ref, shallowReactive} from "vue";
import Budgetpie from "../charts/budgetpie.vue";
import BasicCard from "./BidBasic.vue";

const props = defineProps({
  pid: Number,
})
const id = props.pid
</script>

<style scoped>

</style>