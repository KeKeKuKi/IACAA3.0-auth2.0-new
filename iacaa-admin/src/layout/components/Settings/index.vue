<template>
  <div class="drawer-container">
    <div>
      <h3 class="drawer-title">系统设置</h3>

      <div class="drawer-item">
        <span>系统编辑年份(刷新页面返回今年)</span>
        <el-input-number v-model="year" :min="2000" :max="new Date().getFullYear()+20" label="年份" @change="saveYear"></el-input-number>
      </div>

      <div class="drawer-item">
        <span>主题颜色</span>
        <theme-picker style="float: right;height: 26px;margin: -3px 8px 0 0;" @change="themeChange" />
      </div>

      <div class="drawer-item">
        <span>打开Tag视图</span>
        <el-switch v-model="tagsView" class="drawer-switch" />
      </div>

<!--      <div class="drawer-item">-->
<!--        <span>Fixed Header</span>-->
<!--        <el-switch v-model="fixedHeader" class="drawer-switch" />-->
<!--      </div>-->

      <div class="drawer-item">
        <span>显示Logo</span>
        <el-switch v-model="sidebarLogo" class="drawer-switch" />
      </div>

    </div>
  </div>
</template>

<script>
import ThemePicker from '@/components/ThemePicker'
import router from "@/router";

export default {
  components: { ThemePicker },
  data() {
    return {
      year: this.$store.state.settings.editYear
    }
  },
  computed: {
    fixedHeader: {
      get() {
        return this.$store.state.settings.fixedHeader
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'fixedHeader',
          value: val
        })
      }
    },
    tagsView: {
      get() {
        return this.$store.state.settings.tagsView
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'tagsView',
          value: val
        })
      }
    },
    sidebarLogo: {
      get() {
        return this.$store.state.settings.sidebarLogo
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'sidebarLogo',
          value: val
        })
      }
    }
  },
  methods: {
    themeChange(val) {
      this.$store.dispatch('settings/changeSetting', {
        key: 'theme',
        value: val
      })
    },
    saveYear(){
      localStorage.setItem('editYear', this.year)
      this.$store.dispatch('settings/changeSetting', {
        key: 'editYear',
        value: localStorage.getItem('editYear')
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.drawer-container {
  padding: 24px;
  font-size: 14px;
  line-height: 1.5;
  word-wrap: break-word;

  .drawer-title {
    margin-bottom: 12px;
    color: rgba(0, 0, 0, .85);
    font-size: 14px;
    line-height: 22px;
  }

  .drawer-item {
    color: rgba(0, 0, 0, .65);
    font-size: 14px;
    padding: 12px 0;
  }

  .drawer-switch {
    float: right
  }
}
</style>
