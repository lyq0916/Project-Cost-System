<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="table-header">
          <costpie :pid="pid"></costpie>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {getCurrentInstance, inject, reactive, ref} from "vue";
import Costpie from "./charts/costpie.vue";

</script>

<style scoped>

</style>