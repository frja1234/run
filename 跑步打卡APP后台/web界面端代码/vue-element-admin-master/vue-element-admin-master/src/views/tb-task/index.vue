<template>
  <div class="app-container">
    <div class="filter-container">

      <el-input v-model="query.taskName"
                placeholder="任务名称"
                style="width: 200px;"
                class="filter-item" />

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
        发布任务
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
        <InsertDiagTask :visible.sync="InsertDiagInfo.visible"
                        @getTableData="getTableData()">
        </InsertDiagTask>
      </el-dialog>

      <!-- 修改窗口的页面 -->
      <el-dialog :title="UpdateDiagInfo.title"
                 v-if="UpdateDiagInfo.visible"
                 :visible.sync="UpdateDiagInfo.visible"
                 center
                 :append-to-body='true'
                 :lock-scroll="false"
                 width="80%"
                 @close="handleCloseUpdateDiag">
        <!-- 关闭子窗口执行的函数 -->
        <UpdateDiagTask :diagTaskId="UpdateDiagInfo.diag_task_id"
                        :visible.sync="UpdateDiagInfo.visible"
                        @getTableData="getTableData">
          <!-- :visible.sync双向绑定数据 -->
        </UpdateDiagTask>
      </el-dialog>

      <!-- 学生完成进度窗口的页面 -->
      <el-dialog :title="ShowStudentDiagInfo.title"
                 v-if="ShowStudentDiagInfo.visible"
                 :visible.sync="ShowStudentDiagInfo.visible"
                 center
                 :append-to-body='true'
                 :lock-scroll="false"
                 width="80%"
                 @close="handleCloseShowStudentDiag">
        <!-- 关闭子窗口执行的函数 -->
        <ShowStudentDiagTask :diagTaskId="ShowStudentDiagInfo.diag_task_id"
                             :diagTaskName="ShowStudentDiagInfo.diag_task_name"
                             :diagTaskRequireDistance="ShowStudentDiagInfo.diag_task_require_distance"
                             :diagTaskRequireTime="ShowStudentDiagInfo.diag_task_require_time"
                             :diagTaskDeadline="ShowStudentDiagInfo.diag_task_deadline"
                             :diagSelectClasses="ShowStudentDiagInfo.diag_select_classes"
                             :visible.sync="ShowStudentDiagInfo.visible"
                             @getTableData="getTableData">
          <!-- :visible.sync双向绑定数据 -->
        </ShowStudentDiagTask>
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
      <el-table-column prop="taskName"
                       label="任务名称"
                       width="250" />

      <el-table-column prop="taskRequireDistance"
                       label="要求路程（米）"
                       width="150" />
      <el-table-column prop="taskRequireTime"
                       label="要求时间（min）"
                       width="150">
      </el-table-column>
      <el-table-column label="截止时间"
                       width="180"
                       prop="taskDeadline"
                       :formatter="timestampToTime">
      </el-table-column>
      <el-table-column label="是否过期"
                       width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isOvertime === 0"
                  effect="dark">未过期</el-tag>
          <el-tag v-else
                  type="info"
                  effect="dark">已过期</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini"
                     type="primary"
                     plain
                     @click="handleShowStudent(scope.row.taskId, scope.row.taskName,
                                              scope.row.taskRequireDistance, 
                                              scope.row.taskRequireTime,
                                              scope.row.taskDeadline,
                                              scope.row.selectClasses)">查看完成进度</el-button>
          <el-button size="mini"
                     type="success"
                     plain
                     @click="handleUpdate(scope.row.taskId)">编辑</el-button>
          <el-button size="mini"
                     type="danger"
                     plain
                     @click="handleDelete(scope.row.taskId, scope.row.taskName)">删除</el-button>
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
import InsertDiagTask from './insert.vue' // 引入发布任务页面
import UpdateDiagTask from './update.vue' // 引入修改任务页面
import ShowStudentDiagTask from './showStudent.vue' // 引入修改任务页面

export default {
  components: {
    InsertDiagTask,
    UpdateDiagTask,
    ShowStudentDiagTask
  },
  data () {
    return {
      // 插入窗口的信息
      InsertDiagInfo: {
        // 弹出层是否展示， 默认是false， true为展示
        visible: false,
        title: '',
      },

      // 插入窗口的信息
      UpdateDiagInfo: {
        // 弹出层是否展示， 默认是false， true为展示
        visible: false,
        title: '',
        diag_task_id: 0,
      },

      // 完成进度窗口的信息
      ShowStudentDiagInfo: {
        // 弹出层是否展示， 默认是false， true为展示
        visible: false,
        title: '',
        diag_task_id: 0,
        diag_task_name: '',
        diag_task_require_distance: 0,
        diag_task_require_time: 0,
        diag_task_deadline: '',
        diag_select_classes: '',
      },

      // 数据集合
      tableData: [],
      // 查询对象
      query: {
        taskName: '',
        size: 10,
        current: 1,
        total: 0,
        pages: 1,
      },
    }
  },
  created: function () {
    this.getTableData() //需要触发的函数
  },
  mounted: function () {

  },
  methods: {
    // 查询数据集
    getTableData () {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      request({
        url: '/api/tb-task/select',
        method: 'get',
        params: {
          size: this.query.size,
          current: this.query.current,
          taskName: this.query.taskName,
        }
      }).then(res => {
        let data = res.data
        this.query.total = data.total
        this.query.size = data.size
        this.query.current = data.current
        this.query.pages = data.pages
        // 设置展示的数据集合
        this.tableData = data.records
      })
      loading.close()
    },
    // 处理查询
    handleQuery () {
      this.getTableData()
    },

    // 处理重置查询条件
    handleReSet () {
      this.query.taskName = ''
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

    // 关闭修改子窗口后执行的函数
    handleCloseShowStudentDiag () {
      this.ShowStudentDiagInfo.visible = false
    },

    // 处理插入操作
    handleCreate () {
      this.InsertDiagInfo.title = '发布新任务' // 设置题目
      this.InsertDiagInfo.visible = true; // 默认页面不显示为false,点击按钮将这个属性变成true
    },

    // 处理修改操作
    handleUpdate (taskId) {
      this.UpdateDiagInfo.title = '编辑任务' // 设置题目
      this.UpdateDiagInfo.visible = true; // 默认页面不显示为false,点击按钮将这个属性变成true
      this.UpdateDiagInfo.diag_task_id = taskId
    },

    // 处理查看学生进度操作
    handleShowStudent (taskId, taskName, taskRequireDistance,
      taskRequireTime, taskDeadline, selectClasses) {
      this.ShowStudentDiagInfo.title = '查看完成进度' // 设置题目
      this.ShowStudentDiagInfo.visible = true; // 默认页面不显示为false,点击按钮将这个属性变成true
      this.ShowStudentDiagInfo.diag_task_id = taskId
      this.ShowStudentDiagInfo.diag_task_name = taskName
      this.ShowStudentDiagInfo.diag_task_require_distance = taskRequireDistance
      this.ShowStudentDiagInfo.diag_task_require_time = taskRequireTime
      this.ShowStudentDiagInfo.diag_task_deadline = taskDeadline
      this.ShowStudentDiagInfo.diag_select_classes = selectClasses
    },

    // 处理删除操作, 不可逆操作, 注意！！！！！！
    handleDelete (taskId, taskName) {
      this.$confirm('此操作为不可逆操作，将永久删除任务：【' + taskName + '】 , 是否继续?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request({
          url: '/api/tb-task/delete',
          method: 'delete',
          params: {
            // 参数传递
            taskId: taskId,
          }
        }).then(res => {
          if (res.code === Global.statusCode['SUCCESS_2000']) {
            this.$message({
              message: '任务删除' + res.message,
              type: 'success'
            })
            // 刷新页面
            this.getTableData()
          } else {
            this.$message.error('任务删除失败, 发生不知名错误');
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

