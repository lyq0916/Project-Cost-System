<template>
  <div class="sidebar">
    <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" background-color="#324157"
             text-color="#f0f0f0" active-text-color="#20a0ff" unique-opened router>
      <template v-for="item in menus">
        <template v-if="item.children.length > 0">
          <el-submenu :index="item.path" :key="item.path">
            <template #title>
              <i :class="item.icon"></i>
              <span>{{ item.name }}</span>
            </template>
            <template v-for="subItem in item.children">
              <el-submenu v-if="subItem.children.length > 0" :index="subItem.path" :key="subItem.path">
                <template #title>{{ subItem.name }}</template>
                <el-menu-item v-for="(threeItem, i) in subItem.children" :key="i" :index="threeItem.path">
                  {{ threeItem.name }}
                </el-menu-item>
              </el-submenu>
              <!-- 展示负责的项目   -->
              <el-submenu v-else-if="subItem.path=='/myprojectshow'" :index="subItem.path" :key="subItem.path">
                <template #title>{{ subItem.name }}</template>
                <el-menu-item v-for="(p,i) in projects" :key="i" :index="'/myprojectshow?pid='+p.pid" :route="{path:subItem.path,query:{name:p.pid}}">
                  {{ p.pname }}
<!--             :index="subItem.path"  :route="{path:subItem.path,query:{name:p.pid}}"   -->
                </el-menu-item>
              </el-submenu>
              <el-menu-item v-else :index="subItem.path" :key="subItem.path">
                {{ subItem.name }}
              </el-menu-item>
            </template>
          </el-submenu>
        </template>
        <template v-else>
          <el-menu-item :index="item.path" :key="item.path">
            <i :class="item.icon"></i>
            <template #title>{{ item.name }}</template>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script>
import {computed} from "vue";
import {useStore} from "vuex";
import {useRoute} from "vue-router";

export default {
  setup() {
    const menus = computed(() => {
      return store.state.menus;
    })

    const projects = computed(() => {
      console.log(store.state.project)
      return store.state.project;
    })
    const route = useRoute();

    const onRoutes = computed(() => {
      return route.path;
    });

    const store = useStore();
    const collapse = computed(() => store.state.collapse);

    const project = [
      {id: 1, path: '/myprojectshow', name: '项目1'},
      {id:2,path: '/myprojectshow',name:'项目2'}
    ]
    return {
      menus,
      projects,
      onRoutes,
      collapse,
      project,
    };
  },
};
</script>

<style scoped>
.sidebar {
  display: block;
  position: absolute;
  left: 0;
  top: 70px;
  bottom: 0;
  overflow-y: scroll;
}

.sidebar::-webkit-scrollbar {
  width: 0;
}

.sidebar-el-menu:not(.el-menu--collapse) {
  width: 250px;
}

.sidebar > ul {
  height: 100%;
}
</style>
