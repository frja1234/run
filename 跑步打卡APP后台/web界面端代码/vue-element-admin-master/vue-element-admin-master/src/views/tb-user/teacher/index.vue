<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="query.userNum"
                placeholder="教工号"
                style="width: 200px;"
                class="filter-item" />

      <el-input v-model="query.userRealName"
                placeholder="教师姓名"
                style="width: 200px; margin-left: 10px;"
                class="filter-item" />

      <el-input v-model="query.userCellPhone"
                placeholder="电话号码"
                style="width: 200px; margin-left: 10px;"
                class="filter-item" />

      <el-select v-model="query.defFlag"
                 placeholder="用户状态"
                 clearable
                 class="filter-item"
                 style="width: 130px; margin-left: 10px;">
        <el-option v-for="item in defFlags"
                   :key="item.value"
                   :label="item.label"
                   :value="item.value">
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
        新增教师
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
                 width="50%"
                 @close="handleCloseInsertDiag">
        <!-- 关闭子窗口执行的函数 -->
        <InsertDiagUser :visible.sync="InsertDiagInfo.visible"
                        @getTableData="getTableData('teacher')">
          <!-- :visible.sync双向绑定数据
              @getTableData中的getTableData是子窗口的函数, "getTableData"是父窗口的函数,
              绑定起来, 子窗口通过this.$emit('getTableData') 来调用父窗口的getTableData函数,
              刷新页面 -->
        </InsertDiagUser>
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
        <UpdateDiagUser :visible.sync="UpdateDiagInfo.visible"
                        :diagUserId="UpdateDiagInfo.diagUserId"
                        @getTableData="getTableData('teacher')">
        </UpdateDiagUser>
      </el-dialog>
      <!-- 学生列表窗口的页面 -->
      <el-dialog :title="StudentDiagInfo.title"
                 v-if="StudentDiagInfo.visible"
                 :visible.sync="StudentDiagInfo.visible"
                 center
                 :append-to-body='true'
                 :lock-scroll="false"
                 width="80%"
                 @close="handleCloseStudentDiag">
        <!-- 关闭子窗口执行的函数 -->
        <StudentDiagUser :visible.sync="StudentDiagInfo.visible"
                         :diagUserId="StudentDiagInfo.diagUserId"
                         @getTableData="getTableData('teacher')">
        </StudentDiagUser>
      </el-dialog>
    </div>

    <el-table ref="filterTable"
              :data="tableData.slice((query.current-1)*query.size,query.current*query.size)"
              style="width: 100%"
              stripe
              border>
      <el-table-column fixed="left"
                       type="index"
                       label="序号"
                       width="50">
      </el-table-column>
      <el-table-column fixed="left"
                       prop="userNum"
                       label="教工号"
                       width="150" />

      <el-table-column fixed="left"
                       prop="userRealName"
                       label="教师姓名"
                       width="125" />
      <el-table-column label="头像"
                       fixed="left"
                       width="120">
        <template scope="scope">
          <viewer>
            <img v-if="typeof(scope.row.img) !== 'undefined'"
                 :src="$IMGURL+scope.row.img"
                 style="width: 50px;height: 50px; cursor:pointer" />
            <img v-else
                 src="../../../icons/pic/no_pic.png"
                 style="width: 50px;height: 50px; cursor:pointer" />
          </viewer>
        </template>
      </el-table-column>
      <el-table-column prop="userCellPhone"
                       label="电话号码"
                       width="160" />
      <el-table-column label="身份"
                       width="125">
        <template slot-scope="scope">
          <div slot="reference"
               class="name-wrapper">
            <el-tag size="medium"
                    type="warning"
                    v-if="typeof (scope.row.role.roleName) === 'undefined'">未认证身份</el-tag>
            <el-tag size="medium"
                    v-else>{{ scope.row.role.roleName }}</el-tag>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="最近一次登录时间"
                       width="180"
                       prop="lastLoginTime"
                       :formatter="timestampToTime">
      </el-table-column>
      <el-table-column prop="defFlag"
                       label="用户状态"
                       width="80">
        <template slot-scope="scope">
          <i class="el-icon-success"
             v-if="!scope.row.defFlag"
             style="color: lightGreen"></i>
          <i class="el-icon-error"
             v-else
             style="color: red"></i>
        </template>
      </el-table-column>
      <el-table-column label="操作"
                       width="450">
        <template slot-scope="scope">
          <el-button size="mini"
                     v-if="!scope.row.defFlag"
                     type="info"
                     plain
                     @click="handleCancel(scope.row.userId)">注销</el-button>
          <el-button size="mini"
                     v-else
                     type="warning"
                     plain
                     @click="handleActivate(scope.row.userId)">激活</el-button>
          <el-button size="mini"
                     type="primary"
                     @click="handleStudentList(scope.row.userId,scope.row.userRealName)">学生列表</el-button>
          <el-button size="mini"
                     type="primary"
                     plain
                     @click="handleReSetPass(scope.row.userId, scope.row.userRealName)">重置密码</el-button>
          <el-button size="mini"
                     type="success"
                     plain
                     @click="handleUpdate(scope.row.userId)">编辑</el-button>
          <el-button size="mini"
                     type="danger"
                     plain
                     @click="handleDelete(scope.row.userId, scope.row.userRealName, scope.row.role.roleName, scope.row.role.token)">删除</el-button>
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
import Global from '../../../global/Global'
import InsertDiagUser from './insert.vue' // 引入新增用户页面
import UpdateDiagUser from './update.vue' // 引入修改用户页面
import StudentDiagUser from './student.vue' // 引入修改用户页面

export default {
  components: {
    InsertDiagUser,
    UpdateDiagUser,
    StudentDiagUser,
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
        // 弹出层是否展示， 默认是false， true为展示
        visible: false,
        title: '',
        diagUserId: 0,
      },
      // 学生列表窗口的信息
      StudentDiagInfo: {
        // 弹出层是否展示， 默认是false， true为展示
        visible: false,
        title: '',
        diagUserId: 0,
      },
      // 数据集
      tableData: [],
      // 用户状态（查询选择）
      defFlags: [
        {
          value: '0',
          label: '激活状态'
        }, {
          value: '1',
          label: '注销状态'
        }
      ],
      // 查询对象
      query: {
        defFlag: '',
        userNum: '',
        userRealName: '',
        userCellPhone: '',
        size: 10,
        current: 1,
        total: 0,
        pages: 1,
      },
    }
  },
  mounted: function () {
    this.getTableData('teacher') //需要触发的函数
  },
  methods: {

    // 查询数据集
    getTableData (roleToken) {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      request({
        url: '/api/tb-user/select',
        method: 'get',
        params: {
          size: this.query.size,
          current: this.query.current,
          userNum: this.query.userNum,
          userCellPhone: this.query.userCellPhone,
          userRealName: this.query.userRealName,
          defFlag: this.query.defFlag,
          roleToken: roleToken,
        }
      }).then(res => {
        let data = res.data

        this.query.total = data.total // 数据总数
        this.query.size = data.size  // 每页大小
        this.query.current = data.current // 当前页码
        this.query.pages = data.pages  // 总页数
        // 设置展示的数据集合
        this.tableData = data.records
        // console.log(this.tableData, 'index_data_records')
      })
      loading.close()
    },
    // 处理查询
    handleQuery () {
      this.getTableData('teacher')
    },

    // 重置密码
    handleReSetPass (userId, userRealName) {
      this.$confirm('该操作会将用户【' + userRealName + '】的密码重置为默认，是否继续?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request({
          url: '/api/tb-user/reset_pass',
          method: 'put',
          params: {
            userId: userId,
          }
        }).then(res => {
          if (res.code === Global.statusCode['SUCCESS_2000']) {
            this.$message({
              message: '重置密码' + res.message,
              type: 'success'
            })
            // 刷新页面
            this.getTableData('teacher')
          } else {
            this.$message.error(res.message);
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消密码重置操作'
        });
      });
    },

    // 处理重置查询条件
    handleReSet () {
      this.query.userRealName = ''
      this.query.userNum = ''
      this.query.userCellPhone = ''
      this.query.defFlag = ''
    },
    // 修改页码条数
    handleSizeChange (val) {
      this.query.size = val
    },
    // 修改当前页码数(跳转到第几页)
    handleCurrentChange (val) {
      this.query.current = val
    },
    // 处理插入操作
    handleCreate () {
      this.InsertDiagInfo.title = '新增教师' // 设置题目
      this.InsertDiagInfo.visible = true; // 默认页面不显示为false,点击按钮将这个属性变成true
    },

    // 处理修改操作
    handleUpdate (userId) {
      this.UpdateDiagInfo.diagUserId = userId
      this.UpdateDiagInfo.title = '编辑教师' // 设置题目
      this.UpdateDiagInfo.visible = true; // 默认页面不显示为false,点击按钮将这个属性变成true
    },
    // 处理学生列表
    handleStudentList (userId, userRealName) {
      this.StudentDiagInfo.diagUserId = userId
      this.StudentDiagInfo.title = '教师:' + userRealName + ' - 学生列表' // 设置题目
      this.StudentDiagInfo.visible = true; // 默认页面不显示为false,点击按钮将这个属性变成true
    },


    // 处理注销操作
    handleCancel (userId) {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      request({
        url: '/api/tb-user/cancel',
        method: 'put',
        params: {
          // 参数传递
          userId: userId,
        }
      }).then(res => {
        loading.close()
        if (res.code === Global.statusCode['SUCCESS_2000']) {
          this.$message({
            message: '用户注销' + res.message,
            type: 'success'
          })
          // 刷新页面
          this.getTableData('teacher')
        } else {
          this.$message.error('用户注销失败, 发生不知名错误');
        }
      })
    },

    // 处理激活操作
    handleActivate (userId) {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      request({
        url: '/api/tb-user/activate',
        method: 'put',
        params: {
          // 参数传递
          userId: userId,
        }
      }).then(res => {
        loading.close()
        if (res.code === Global.statusCode['SUCCESS_2000']) {
          this.$message({
            message: '用户激活' + res.message,
            type: 'success'
          })
          // 刷新页面
          this.getTableData('teacher')
        } else {
          this.$message.error('用户激活失败, 发生不知名错误');
        }
      })
    },

    // 处理删除操作, 不可逆操作, 注意！！！！！！
    handleDelete (userId, userRealName, roleName, token) {
      if (typeof (roleName) === 'undefined') {
        roleName = '未认证身份'
      }
      this.$confirm('此操作为不可逆操作，将永久删用户：' + userRealName + ' 【' + roleName + '】 , 是否继续?', '警告', {
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
          url: '/api/tb-user/delete',
          method: 'delete',
          params: {
            // 参数传递
            userId: userId,
            roleToken: token, // 用于判断是否是老师，如果是教师的删除，需要把相应的学生的教师id设为null
          }
        }).then(res => {
          loading.close()
          if (res.code === Global.statusCode['SUCCESS_2000']) {
            this.$message({
              message: '用户删除' + res.message,
              type: 'success'
            })
            // 刷新页面
            this.getTableData('teacher')
          } else {
            this.$message.error('用户删除失败, 发生不知名错误');
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除操作'
        });
      });
    },

    // 关闭新增子窗口后执行的函数
    handleCloseInsertDiag () {
      this.InsertDiagInfo.visible = false
    },

    // 关闭修改子窗口后执行的函数
    handleCloseUpdateDiag () {
      this.UpdateDiagInfo.visible = false
    },

    // 关闭学生列表子窗口后执行的函数
    handleCloseStudentDiag () {
      this.StudentDiagInfo.visible = false
    },
    // 时间戳转换成时间
    // 使用element table组件中的formatter属性，传入一个函数
    timestampToTime (row, column) {
      if (typeof (row.lastLoginTime) === 'undefined') {
        return '暂无时间'
      } else {
        var date = new Date(row.lastLoginTime) //时间戳为10位需*1000，时间戳为13位的话不需乘1000
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

