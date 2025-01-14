<template>
  <div class="container_textarea">
    <ShowBox>
<!--      <div class="text_font">发布者：</div>-->
<!--      <div style="margin-top: 5px;margin-bottom: 15px">-->
<!--        <el-input v-model="new_push" style="width: 95%;font-size: 16px" placeholder="填写发布者" />-->
<!--      </div>-->


<!--      <div class="text_font">新闻标题：</div>-->
<!--      <div style="margin-top: 5px;margin-bottom: 15px">-->
<!--        <el-input v-model="new_title" style="width: 95%;font-size: 16px" placeholder="填写新闻标题" />-->
<!--      </div>-->


<!--      <div class="text_font">新闻内容：</div>-->
<!--      <div style="margin-top: 5px">-->
<!--        <el-input-->
<!--            v-model="new_content"-->
<!--            style="width: 95%;font-size: 16px"-->
<!--            :rows="13"-->
<!--            type="textarea"-->
<!--            placeholder="填写新闻内容"-->
<!--        />-->
<!--      </div>-->
<!--      <div style="margin-top: 18px;text-align: center">-->
<!--        <el-button type="primary">提交</el-button>-->
<!--        <el-button type="info" @click="clear">重置</el-button>-->
<!--      </div>-->
      <el-form ref="formRef" size="default" label-width="78px">
        <el-row gutter="15">
          <el-col :span="8">
            <el-form-item label="新闻标题" prop="new_title">
              <el-input v-model="new_title" type="text" placeholder="请输入标题新闻标题" clearable
                        :style="{width: '100%'}"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="发布者" prop="new_push">
              <el-input v-model="new_push" type="text" placeholder="请输入发布者" clearable
                        :style="{width: '100%'}"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="日期选择" prop="new_time">
              <el-date-picker v-model="new_time" format="YYYY-MM-DD" value-format="YYYY-MM-DD"
                              :style="{width: '100%'}" placeholder="请选择日期选择" clearable></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="新闻内容" prop="new_content">
          <el-input v-model="new_content" type="textarea" placeholder="请输入新闻内容" show-word-limit
                    :autosize="{minRows: 21, maxRows: 60}" :style="{width: '100%'}"></el-input>
        </el-form-item>

          <div style="text-align: center">
            <el-button type="primary" @click="tj">提交</el-button>
            <el-button type="info" @click="clear">重置</el-button>
          </div>

      </el-form>
    </ShowBox>
  </div>
</template>

<script>
import ShowBox from "../../components/News/ShowBox";
import {insertNews} from "../../api/news";

export default {
  name: "new_Main",
  components: {ShowBox},
  data(){
    return{
      new_content:"",
      new_title:"",
      new_push:"",
      new_time:"",
    };
  },
  methods:{
    clear(){
      this.new_content="";
      this.new_title="";
      this.new_push="";
      this.new_time="";
    },
    tj(){
      if(this.new_content!=null&&this.new_title!=null&&this.new_push!=null&&this.new_time!=null){
        insertNews(this.new_title,this.new_content,this.new_push,this.new_time).then(response=>{
          console.log(response)
          if(response=="success"){
            this.$message({
              message: '添加成功',
              type: 'success'
            });
          }else{
            this.$message({
              message: '添加失败',
              type: 'error'
            });
          }
        });
      }
      else {
        this.$message({
          message: '内容未填写不允许提交',
          type: 'error'
        });
      }

    }
  }
}
</script>

<style scoped lang="scss">
.container_textarea {
  margin-top: 10px; /* 距离上方15px */
  margin-left: 10px; /* 距离左侧10px */
  margin-right: 15px; /* 距离右侧10px */
  padding: 5px; /* 可选：内容与容器边框的距离 */
}
.text_font{
  font-size: 20px;
  position: relative;
  font-family: AlimamaShuHeiTi;
  margin-bottom: 10px;
  margin-left: 3px;
}

</style>