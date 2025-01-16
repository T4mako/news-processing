<template>
  <div class="container_textarea">
    <ShowBox>
      <el-row :gutter="5">
        <el-col :span="8" ><div class="text_font">新闻标题：{{formData.title}}</div></el-col>
        <el-col :span="6"><div class="text_font">发布人：{{formData.push_name}}</div></el-col>
        <el-col :span="6"><div class="text_font">发布时间：{{formData.time}}</div></el-col>
        <el-col :span="4"><div class="text_font">点击数：{{formData.click_num}}</div></el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8"><div class="text_font">新闻分类：{{formData.category}}</div></el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :span="1.5" ><div class="text_font">新闻内容:</div></el-col>
        <el-col :span="22">
          <el-input
              v-model="formData.content"
              style="width: 100%"
              :autosize="{ minRows: 2, maxRows: 15 }"
              type="textarea"
              placeholder="Please input"
              readonly
          />
        </el-col>
      </el-row>
    </ShowBox>

    <el-row :gutter="20">
      <el-col :span="12">
        <ShowBox>
          <div style="margin-top: 20px">
            <Charts :options="wordOptions" />
          </div>
        </ShowBox>
      </el-col>
      <el-col :span="12">
        <ShowBox>
          <div style="margin-top: 20px">
          <Charts :options="barOptions" /> <!--  词频统计柱状图-->
          </div>
        </ShowBox>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <ShowBox>
          <div style="margin-top: 20px">
            <Charts :options="pieOptions" /><!--  新闻分类饼状图图-->
          </div>
        </ShowBox>
      </el-col>
      <el-col :span="12">
        <ShowBox>
          <div style="margin-top: 20px">
            <Charts :options="newOptions" /><!--  新闻分类柱状图-->
          </div>

        </ShowBox>
      </el-col>
    </el-row>

  </div>

</template>

<script >
import * as echarts from 'echarts'; // 正确引入 echarts
import ShowBox from "../../components/News/ShowBox";
import {getNewsById} from "../../api/news";
import Charts from "../../components/News/Charts";
import 'echarts-wordcloud';
export default {
  name: "index",
  components: {ShowBox,Charts},
  data() {
    return {
      id:"",
      formData: {
        title: '',
        push_name: '',
        time: '',
        category: '',
        content: '',
        click_num:'',
      },
      //关键字词云图
      wordData: [
        {name: 'web',value: 84},
        {name: 'GIT',value: 5},
        {name: 'CSS',value: 22},
        {name: 'CSS',value: 11},
        {name: '前端',value: 101},
        {name: 'CSS',value: 33},
        {name: 'Vue',value: 77},
        {name: 'js',value: 98 },
        {name: '互联网',value: 66},
       {name: '插件',value: 55},
      ],
      wordOptions: {},
      //词频统计柱状图
      xData: ['点', '击', '柱', '子', '或', '者', '两', '指', '在', '触', '屏', '上', '滑', '动', '能', '够'],
      barData:[220, 182, 191, 234, 290, 330, 310, 123, 442,  122, 133, 334, 198, 123, 125, 220],
      barOptions: {},
      //新闻分类饼状图
      pieData: [
        {value: 335, name: '体育'},
        {value: 310, name: '时尚'},
        {value: 234, name: '财经'},
        {value: 135, name: '政治'},
        {value: 1548, name: '国外'}
      ],
      pieOptions: {},
      //新闻分类柱状图
      newXData: ['体育', '时尚', '财经', '政治', '国外'],
      newData:[220, 182, 191, 234, 290],
      newOptions: {},

    }
  },
  methods: {
    getNews(){
      getNewsById(this.id).then(response => {
        console.log(response)
        this.formData = response;
      })
    },
    //获取关键词词云图信息
    getWordData(wordData){
      this.wordOptions={
        title: {
          text: '热点词',
        },
        tooltip: {
          show: true
        },
        series: [
          {
            //用来调整词之间的距离
            gridSize: 20,
            //用来调整字的大小范围
            sizeRange: [18, 40],
            //用来调整词的旋转方向，，[0,0]--代表着没有角度，也就是词为水平方向
            rotationRange: [0, 0],
            type: 'wordCloud',
            size: ['95%', '95%'],
            textRotation: [0, 95],
            textPadding: 0,
            autoSize: {
              enable: true,
              minSize: 14
            },
            //全局文本样式
            textStyle: {
              normal: {
                // Color可以是一个回调函数或一个颜色字符串
                color: function () {
                  const color = `rgb(${[
                    Math.round(Math.random() * 160),
                    Math.round(Math.random() * 160),
                    Math.round(Math.random() * 160),
                  ].join(",")})`;
                  console.log("颜色生成")
                  console.log("Generated Color:", color); // 打印生成的颜色
                  return color;
                },
              },
              // 移入文本效果
              emphasis: {
                textShadowBlur: 20,
                textShadowColor: "#409EFF",
              },
            },
            data: wordData,
          },
        ],
      }
    },
    //获取词频统计柱状图
    getBarData(barData,xData){
      this.barOptions={
        title: {
          text: '词频统计'
        },
        tooltip: {},
        grid: {
          bottom: '5%' // 柱状图距离容器底部的距离
        },
        xAxis: {
          type: 'category',
          data: xData,
          axisLabel: {
            inside: true,
            color: '#fff'
          },
          axisTick: {
            show: false
          },
          axisLine: {
            show: false
          },
          z: 10
        },
        yAxis: {
          type: 'value',
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            color: '#999'
          }
        },
        series: [
          {
            name: '出现频率',
            type: 'bar',
            showBackground: true,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#83bff6' },
                { offset: 0.5, color: '#188df0' },
                { offset: 1, color: '#188df0' }
              ])
            },
            emphasis: {
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#2378f7' },
                  { offset: 0.7, color: '#2378f7' },
                  { offset: 1, color: '#83bff6' }
                ])
              }
            },
            data: barData,
          }
        ]
      }
    },
    //获取新闻分类饼状图信息
    getPieData(pieData){
      this.pieOptions= {
        title: {
          text: '新闻分类',
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '15%',
          left: 'center'
        },
        series: [
          {
            name: '新闻分类比例',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '65%'],
            avoidLabelOverlap: false,
            padAngle: 5,
            itemStyle: {
              borderRadius: 10
            },
            label: {
              show: false,
              position: 'center',
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 40,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: pieData,
          }
        ]
      }
    },
    //获取新闻分类柱状图
    getNewData(newData,newXData){
      this.newOptions={
        title: {
          text: '新闻分类比例'
        },
        tooltip: {},
        grid: {
          bottom: '5%' // 柱状图距离容器底部的距离
        },
        xAxis: {
          type: 'category',
          data: newXData,
          axisLabel: {
            inside: true,
            color: '#fff'
          },
          axisTick: {
            show: false
          },
          axisLine: {
            show: false
          },
          z: 10
        },
        yAxis: {
          type: 'value',
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            color: '#999'
          }
        },
        series: [
          {
            name: '出现频率',
            type: 'bar',
            showBackground: true,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#83bff6' },
                { offset: 0.5, color: '#188df0' },
                { offset: 1, color: '#188df0' }
              ])
            },
            emphasis: {
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#2378f7' },
                  { offset: 0.7, color: '#2378f7' },
                  { offset: 1, color: '#83bff6' }
                ])
              }
            },
            data: newData
          }
        ]
      }
    },
  },
  mounted() {
    this.id=this.$route.query.id;
    console.log(this.id)
    this.getNews();
    //加载词云图数据
    this.getWordData(this.wordData)
    //加载词频统计柱状图数据
    this.getBarData(this.barData,this.xData)
    //加载新闻分类饼状图数据
    this.getPieData(this.pieData)
    //加载新闻分类柱状图数据
    this.getNewData(this.newData,this.newXData)
  }
}
</script>


<style scoped lang="scss">
.container_textarea {
  margin-top: 5px; /* 距离上方15px */
  margin-left: 10px; /* 距离左侧10px */
  margin-right: 10px; /* 距离右侧10px */
  padding: 5px; /* 可选：内容与容器边框的距离 */
}
.text_font{
  //font-size: 20px;
  position: relative;
  font-family: SmileySans;
  padding: 2px;

}
.el-row {
  margin-bottom: 20px;
}
.el-row:last-child {
  margin-bottom: 0;
}
.el-col {
  border-radius: 4px;
}
.card{
  width: 50%;
}


</style>
