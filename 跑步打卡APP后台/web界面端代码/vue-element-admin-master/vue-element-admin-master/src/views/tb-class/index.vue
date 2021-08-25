<template>
  <div class="app-container">
    <div class="filter-container">

      <el-input v-model="query.className"
                placeholder="班级名称"
                style="width: 300px;"
                class="filter-item" />
      <el-select v-model="query.collegeId"
                 placeholder="所属学院"
                 clearable
                 class="filter-item"
                 style="width: 480px; margin-left: 10px;">
        <el-option v-for="item in query.colleges"
                   :key="item.collegeId"
                   :label="item.collegeName"
                   :value="item.collegeId">
        </el-option>
      </el-select>

      <el-button v-waves
                 class="filter-item"
                 style="margin-left: 10px;"
                 type="success"
                 icon="el-icon-search"
                 @click="handleQuery">
        查询
      </el-button>
      <el-button class="filter-item"
                 style="margin-left: 10px;"
                 type="primary"
                 icon="el-icon-edit"
                 @click="handleCreate">
        新增班级
      </el-button>
      <el-button class="filter-item"
                 style="margin-left: 10px;"
                 type="info"
                 icon="el-icon-refresh-left"
                 @click="handleReSet">
        重置查询条件
      </el-button>
      <!-- 新增窗口的页面 -->
      <el-dialog :title="InsertDiagInfo.title"
                 v-if="InsertDiagInfo.visible"
                 :visible.sync="InsertDiagInfo.visible"
                 center
                 :append-to-body='true'
                 :lock-scroll="false"
                 width="80%"
                 @close="handleCloseInsertDiag">
        <!-- 关闭子窗口执行的函数 -->
        <InsertDiagClass :visible.sync="InsertDiagInfo.visible"
                         @getTableData="getTableData()">
        </InsertDiagClass>
      </el-dialog>

      <!-- 修改窗口的页面 -->
      <el-dialog :title="UpdateDiagInfo.title"
                 v-if="UpdateDiagInfo.visible"
                 :visible.sync="UpdateDiagInfo.visible"
                 center
                 :append-to-body='true'
                 :lock-scroll="false"
                 width="50%"
                 @close="handleCloseUpdateDiag">
        <!-- 关闭子窗口执行的函数 -->
        <UpdateDiagClass :diagClassId="UpdateDiagInfo.diag_class_id"
                         :visible.sync="UpdateDiagInfo.visible"
                         @getTableData="getTableData">
          <!-- :visible.sync双向绑定数据 -->
        </UpdateDiagClass>
      </el-dialog>
    </div>

    <el-table ref="filterTable"
              :data="tableData.slice((query.current-1)*query.size,query.current*query.size)"
              style="width: 100%"
              stripe
              border>
      <el-table-column type="index"
                       label="序号"
                       width="50">
      </el-table-column>
      <el-table-column prop="className"
                       label="班级名称"
                       width="280" />

      <el-table-column prop="classCode"
                       label="班级代码"
                       width="150" />
      <el-table-column label="所属学院"
                       width="300">
        <template slot-scope="scope">

          <div slot="reference"
               class="name-wrapper">
            <el-tag v-if="typeof(scope.row.college) !== 'undefined'"
                    size="medium">{{ scope.row.college.collegeName }}</el-tag>
            <el-tag v-else
                    type="danger"
                    size="medium">暂无二级学院</el-tag>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template slot-scope="scope">

          <el-button size="mini"
                     type="success"
                     plain
                     @click="handleUpdate(scope.row.classId)">编辑</el-button>
          <el-button size="mini"
                     type="danger"
                     plain
                     @click="handleDelete(scope.row.classId, scope.row.className, scope.row.college.collegeName)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block">
      <el-pagination @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"
                     :current-page="query.current"
                     :page-sizes="[3, 5, 10, 20]"
                     :page-size="query.size"
                     layout="total, sizes, prev, pager, next, jumper"
                     :total="query.total">
      </el-pagination>
    </div>
  </div>

</template>

<script>
import request from '@/utils/request' // request请求 js
import Global from '../../global/Global'
import InsertDiagClass from './insert.vue' // 引入新增班级页面
import UpdateDiagClass from './update.vue' // 引入修改班级页面

export default {
  components: {
    InsertDiagClass,
    UpdateDiagClass,
  },
  data () {
    return {
      // 插入窗口的信息
      InsertDiagInfo: {
        // 弹出层是否展示， 默认是false， true为展示
        visible: false,
        title: '',
      },

      // 修改窗口的信息
      UpdateDiagInfo: {
        diag_class_id: 0, // 传入当前班级的id，用于修改子窗口重新查询信息
        // 弹出层是否展示， 默认是false， true为展示
        visible: false,
        title: '',
      },

      // 数据集合
      tableData: [],
      // 查询对象
      query: {
        className: '',
        collegeId: '',
        colleges: [],
        size: 10,
        current: 1,
        total: 0,
        pages: 1,
      },
    }
  },
  created: function () {
    this.getCollegeAll()
  },
  mounted: function () {
    this.getTableData() //需要触发的函数
  },
  methods: {

    // 查询所有得学院信息
    getCollegeAll () {
      request({
        url: '/api/tb-college/select_all',
        method: 'get',
      }).then(res => {
        let data = res.data
        this.query.colleges = data
      })
    },

    // 查询数据集
    getTableData () {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      // loading.close();
      request({
        url: '/api/tb-class/select',
        method: 'get',
        params: {
          size: this.query.size,
          current: this.query.current,
          className: this.query.className,
          collegeId: this.query.collegeId,
        }
      }).then(res => {
        let data = res.data
        this.query.total = data.total
        this.query.size = data.size
        this.query.current = data.current
        this.query.pages = data.pages
        // 设置展示的数据集合
        this.tableData = data.records
        loading.close();
      })
    },
    // 处理查询
    handleQuery () {
      this.getTableData()
    },

    // 处理重置查询条件
    handleReSet () {
      this.query.className = ''
      this.query.collegeId = ''
    },
    // 修改页码条数
    handleSizeChange (val) {
      this.query.size = val
    },
    // 修改当前页码数(跳转到第几页)
    handleCurrentChange (val) {
      this.query.current = val
    },
    // 关闭新增子窗口后执行的函数
    handleCloseInsertDiag () {
      this.InsertDiagInfo.visible = false
    },

    // 关闭修改子窗口后执行的函数
    handleCloseUpdateDiag () {
      this.UpdateDiagInfo.visible = false
    },

    // 处理插入操作
    handleCreate () {
      this.InsertDiagInfo.title = '新增班级' // 设置题目
      this.InsertDiagInfo.visible = true; // 默认页面不显示为false,点击按钮将这个属性变成true
    },

    // 处理修改操作
    handleUpdate (classId) {
      this.UpdateDiagInfo.diag_class_id = classId // 把当前的班级id传入到修改子窗口
      this.UpdateDiagInfo.title = '编辑班级' // 设置题目
      this.UpdateDiagInfo.visible = true; // 默认页面不显示为false,点击按钮将这个属性变成true
    },

    // 处理删除操作, 不可逆操作, 注意！！！！！！
    handleDelete (classId, className, collegeName) {
      this.$confirm('此操作为不可逆操作，将永久删除班级：【' + collegeName + '】' + className + ' , 是否继续?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        request({
          url: '/api/tb-class/delete',
          method: 'delete',
          params: {
            // 参数传递
            classId: classId,
          }
        }).then(res => {
          loading.close()
          if (res.code === Global.statusCode['SUCCESS_2000']) {
            this.$message({
              message: '班级删除' + res.message,
              type: 'success'
            })
            // 刷新页面
            this.getTableData()
          } else {
            this.$message.error('班级删除失败, 发生不知名错误');
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除操作'
        });
      });
    },




    // 时间戳转换成时间
    // 使用element table组件中的formatter属性，传入一个函数
    timestampToTime (row, column) {
      if (typeof (row.taskDeadline) === 'undefined') {
        return '暂无时间'
      } else {
        var date = new Date(row.taskDeadline) //时间戳为10位需*1000，时间戳为13位的话不需乘1000
        var Y = date.getFullYear() + '-'
        var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
        var D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + '  '
        var h = date.getHours() + ':'
        var m = date.getMinutes() + ':'
        var s = date.getSeconds()
        return Y + M + D + h + m + s
      }
    },




  },

}
</script>

