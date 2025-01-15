<template>
  <div class="container_textarea">
    <ShowBox>
      <el-table :data="tableData.filter(data => !search || data.title.toLowerCase().includes(search.toLowerCase())
      ||data.push_name.toLowerCase().includes(search.toLowerCase())
      ||data.category.toLowerCase().includes(search.toLowerCase()))" border style="width: 100%">
        <el-table-column fixed label="新闻标题" prop="title" />
        <el-table-column label="发布者" prop="push_name" />
        <el-table-column label="标签" prop="category" />
        <el-table-column label="发布日期" prop="time" />
        <el-table-column align="center">
          <template #header>
            <el-input v-model="search" placeholder="输入关键字搜索" />
          </template>
          <template #default="scope" >
            <el-button size="small" @click="findDetial(scope.row)">修改</el-button>
            <el-button size="small" type="danger" @click="deleteNews(scope.row)">删除</el-button>
            <el-button size="small" type="primary" @click="findD(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 8px">
        <el-pagination small
                       background layout="->,prev, pager, next"
                       :total="total"
                       v-model:current-page="currentPage"
                       :page-size="pageSize"
                       @current-change="handleCurrentChange"/>
      </div>
    </ShowBox>
  </div>
  <el-dialog v-model="dialogVisible" title="新闻详情">
    <el-form ref="formRef" :model="formData" size="default" label-width="100px"
             label-position="left">
      <el-form-item label="新闻标题" prop="title">
        <el-input v-model="formData.title" type="text" placeholder="请输入新闻标题" clearable
                  :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label="发布者" prop="push_name">
        <el-input v-model="formData.push_name" type="text" placeholder="请输入发布者" clearable
                  :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label="发布时间" prop="time">
        <el-input v-model="formData.time" type="text" placeholder="请输入发布时间" clearable
                  :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label="新闻类别" prop="category">
        <el-input v-model="formData.category" type="text" placeholder="请输入新闻类别" clearable
                  :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label="新闻内容" prop="content">
        <el-input v-model="formData.content" type="textarea" placeholder="请输入新闻内容"
                  :autosize="{minRows: 4, maxRows: 10}" :style="{width: '100%'}"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="closeD">取消</el-button>
      <el-button type="primary" @click="xg">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
import ShowBox from "../../components/News/ShowBox";
import {getAll,deleteNews,getNewsById,updateNews} from "../../api/news";

export default {
  name: "index",
  components: {ShowBox},
  data(){
    return{
      tableData:[],
      tableData2:[],
      search: '',
      filterTableData:[],
      currentPage: 1, // 当前页码
      pageSize: 6, // 每页显示数量
      total: 0, // 总数据量
      dialogVisible: false,
      formData: {
        title: '',
        push_name: '',
        time: '',
        category: '',
        content: '',
      },
    }
  },
  methods: {
    //分页管理
    handleCurrentChange(page) {
      const startIndex = (page - 1) * this.pageSize
      this.tableData = this.tableData2.slice(startIndex, startIndex + this.pageSize)
      this.total = this.tableData2.length
    },
    //获取新闻数据
    getNews(){
      getAll().then(response => {
        // this.tableData = response;
        console.log(response)
        this.tableData2 = response;
        this.handleCurrentChange(this.currentPage)

      })
    },
    //删除操作
    deleteNews(row) {
      console.log(row.id)
      deleteNews(row.id).then(response => {
        if(response=="success"){
          this.$message({
            message: '删除成功',
            type: 'success'
          });
          this.$tab.refreshPage();
          this.clear();


        }
        else{
          this.$message({
            message: '删除失败',
            type: 'error'
          });
        }
      })
    },
    //关闭弹窗
    closeD(){
      this.dialogVisible = false;
    },
    //打开弹窗
    findDetial(row){
      this.dialogVisible = true;
      getNewsById(row.id).then(response => {
        console.log(response)
        this.formData = response;
      })
    },
    //提交修改内容
    xg(){
      updateNews(this.formData.id,this.formData.title,this.formData.content,this.formData.push_name,this.formData.time,this.formData.category).then(response => {
        if(response=="success"){
          this.$message({
            message: '修改成功',
            type: 'success'
          });
          this.$tab.refreshPage();
          this.clear();
        }
        else{
          this.$message({
            message: '修改失败',
            type: 'error'
          });
        }
      })
    },
    //跳转到新闻详情页面
    findD(row){
      this.$router.push({
        path: '/news_data',
        query: {
          id: row.id
        }
      })
    },


  },
  mounted() {
   this.getNews();
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
  font-size: 20px;
  position: relative;
  font-family: AlimamaShuHeiTi;
  //margin-bottom: 5px;
  //margin-left: 3px;
  padding: 8px;
  text-align: right;
}
.input_select{
  padding: 5px;
  text-align: left;
}

</style>