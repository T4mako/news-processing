<template>
  <div ref="chart" :style="{ width: '100%', height: '300px' }"></div>
</template>

<script>
import * as echarts from 'echarts'; // 正确引入 echarts
import 'echarts-wordcloud';
export default {
  name: "Charts",
  props: {
    options: {
      type: Object,
      required: true
    }
  },
  mounted() {
    console.log("检查 DOM 元素是否正确渲染"+this.$refs.chart); // 检查 DOM 元素是否正确渲染
    this.chart = echarts.init(this.$refs.chart);
    this.chart.setOption(this.options);
  },
  watch: {
    options: {
      deep: true,
      handler(newOptions) {
        this.chart.setOption(newOptions);
      }
    }
  },
  beforeDestroy() {
    this.chart.dispose();
  },
}
</script>

<style scoped>

</style>