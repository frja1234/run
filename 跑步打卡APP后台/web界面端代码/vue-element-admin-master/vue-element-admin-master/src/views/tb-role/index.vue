<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="query.roleName"
                placeholder="角色名称"
                style="width: 200px;"
                class="filter-item" />

      <el-select v-model="query.defFlag"
                 placeholder="角色状态"
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
        新增角色
      </el-button>
      <el-button class="filter-item"
                 style="margin-left: 10px;"
                 type="info"
                 icon="el-icon-refresh-left"
                 @click="handleReSet">
        重置查询条件
      </el-button>

    </div>
    <!-- v-if='InsertDiagInfo.visible' 解决了第二次传值还是第一次传值的数据问题 -->
    <el-dialog :title="InsertDiagInfo.title"
               v-if="InsertDiagInfo.visible"
               :visible.sync="InsertDiagInfo.visible"
               center
               :append-to-body='true'
               :lock-scroll="false"
               width="50%"
               @close="handleCloseInsertDiag">
      <!-- 关闭子窗口执行的函数 -->
      <InsertDiagRole :visible.sync="InsertDiagInfo.visible"
                      @getTableData="getTableData">
        <!-- :visible.sync双向绑定数据
              @getTableData中的getTableData是子窗口的函数, "getTableData"是父窗口的函数,
              绑定起来, 子窗口通过this.$emit('getTableData') 来调用父窗口的getTableData函数,
              刷新页面 -->
      </InsertDiagRole>
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
      <UpdateDiagRole :diagRoleId="UpdateDiagInfo.diag_role_id"
                      :visible.sync="UpdateDiagInfo.visible"
                      @getTableData="getTableData">
        <!-- :visible.sync双向绑定数据 -->
      </UpdateDiagRole>
    </el-dialog>

    <!-- 授权窗口的页面 -->
    <el-dialog :title="GrantDiagInfo.title"
               v-if="GrantDiagInfo.visible"
               :visible.sync="GrantDiagInfo.visible"
               center
               :append-to-body='true'
               :lock-scroll="false"
               width="80%"
               @close="handleCloseGrantDiag">
      <!-- 关闭子窗口执行的函数 -->
      <GrantDiagRole :diagRoleId="GrantDiagInfo.diag_role_id"
                     :visible.sync="GrantDiagInfo.visible"
                     @getTableData="getTableData">
        <!-- :visible.sync双向绑定数据 -->
      </GrantDiagRole>
    </el-dialog>

    <el-table ref="filterTable"
              :data="tableData.slice((query.current-1)*query.size,query.current*query.size)"
              style="width: 100%"
              stripe
              border>
      <el-table-column type="index"
                       label="序号"
                       width="50">
      </el-table-column>
      <el-table-column prop="roleCode"
                       label="角色代码"
                       width="150" />

      <el-table-column prop="roleName"
                       label="角色名称"
                       width="200" />
      <el-table-column prop="token"
                       label="角色Token"
                       width="150">
        <template slot-scope="scope">

          <div slot="reference"
               class="name-wrapper">
            <el-tag size="medium">{{ scope.row.token }}</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="remarks"
                       label="角色备注" />
      <el-table-column prop="defFlag"
                       label="角色状态"
                       width="100">
        <template slot-scope="scope">
          <i class="el-icon-success"
             v-if="!scope.row.defFlag"
             style="color: lightGreen"></i>
          <i class="el-icon-error"
             v-else
             style="color: red"></i>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini"
                     v-if="!scope.row.defFlag"
                     type="info"
                     plain
                     @click="handleCancel(scope.row.roleId)">注销</el-button>
          <el-button size="mini"
                     v-else
                     type="warning"
                     plain
                     @click="handleActivate(scope.row.roleId)">激活</el-button>
          <el-button size="mini"
                     type="primary"
                     plain
                     @click="handleGrant(scope.row.roleId,scope.row.roleName)">角色授权</el-button>
          <el-button size="mini"
                     type="success"
                     plain
                     @click="handleUpdate(scope.row.roleId)">编辑</el-button>
          <el-button size="mini"
                     type="danger"
                     plain
                     @click="handleDelete(scope.row.roleId, scope.row.roleCode, scope.row.roleName)">删除</el-button>
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
import InsertDiagRole from './insert.vue' // 引入新增角色页面
import UpdateDiagRole from './update.vue' // 引入修改角色页面
import GrantDiagRole from './grant.vue' // 引入角色授权页面

export default {
  components: {
    InsertDiagRole,
    UpdateDiagRole,
    GrantDiagRole,
  },
  data () {
    return {
      // 插入窗口的信息
      InsertDiagInfo: {
        visible: false,
        title: '',
      },
      // 修改窗口的信息
      UpdateDiagInfo: {
        diag_role_id: -1, // 传入当前角色的id，用于修改子窗口重新查询信息
        // 弹出层是否展示， 默认是false， true为展示
        visible: false,
        title: '',
      },
      // 授权窗口的信息
      GrantDiagInfo: {
        diag_role_id: -1, // 传入当前角色的id，用于授权子窗口查询信息
        // 弹出层是否展示， 默认是false， true为展示
        visible: false,
        title: '',
      },

      // 数据集合
      tableData: [],
      // 权限状态（查询选择）
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
        roleName: '',
        size: 10,
        current: 1,
        total: 0,
        pages: 1,
      },
    }
  },
  mounted: function () {
    this.getTableData() //需要触发的函数
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
        url: '/api/tb-role/select',
        method: 'get',
        params: {
          size: this.query.size,
          current: this.query.current,
          roleName: this.query.roleName,
          defFlag: this.query.defFlag,
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
      this.query.roleName = ''
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
      this.InsertDiagInfo.title = '新增角色' // 设置题目
      this.InsertDiagInfo.visible = true; // 默认页面不显示为false,点击按钮将这个属性变成true
    },

    // 处理修改操作
    handleUpdate (roleId) {
      this.UpdateDiagInfo.diag_role_id = roleId // 把当前的角色id传入到修改子窗口
      this.UpdateDiagInfo.title = '编辑角色' // 设置题目
      this.UpdateDiagInfo.visible = true; // 默认页面不显示为false,点击按钮将这个属性变成true
    },

    // 处理授权操作
    handleGrant (roleId, roleName) {
      this.GrantDiagInfo.diag_role_id = roleId // 把当前的角色id传入到授权子窗口
      this.GrantDiagInfo.title = '角色授权 - ' + roleName  // 设置题目
      this.GrantDiagInfo.visible = true; // 默认页面不显示为false,点击按钮将这个属性变成true
    },

    // 处理注销操作
    handleCancel (roleId) {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      request({
        url: '/api/tb-role/cancel',
        method: 'put',
        params: {
          // 参数传递
          roleId: roleId,
        }
      }).then(res => {
        loading.close()
        if (res.code === Global.statusCode['SUCCESS_2000']) {
          this.$message({
            message: '角色注销' + res.message,
            type: 'success'
          })
          // 刷新页面
          this.getTableData()
        } else {
          this.$message.error('角色注销失败, 发生不知名错误');
        }
      })
    },

    // 处理激活操作
    handleActivate (roleId) {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      request({
        url: '/api/tb-role/activate',
        method: 'put',
        params: {
          // 参数传递
          roleId: roleId,
        }
      }).then(res => {
        loading.close()
        if (res.code === Global.statusCode['SUCCESS_2000']) {
          this.$message({
            message: '角色激活' + res.message,
            type: 'success'
          })
          // 刷新页面
          this.getTableData()
        } else {
          this.$message.error('角色激活失败, 发生不知名错误');
        }
      })
    },

    // 处理删除操作, 不可逆操作, 注意！！！！！！
    handleDelete (roleId, roleCode, roleName) {
      this.$confirm('此操作为不可逆操作，将永久删除角色：' + roleCode + '     ' + roleName + ' , 是否继续?', '警告', {
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
          url: '/api/tb-role/delete',
          method: 'delete',
          params: {
            // 参数传递
            roleId: roleId,
          }
        }).then(res => {
          loading.close()
          if (res.code === Global.statusCode['SUCCESS_2000']) {
            this.$message({
              message: '角色删除' + res.message,
              type: 'success'
            })
            // 刷新页面
            this.getTableData()
          } else {
            this.$message.error('角色删除失败, 发生不知名错误');
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

    // 关闭授权子窗口后执行的函数
    handleCloseGrantDiag () {
      this.GrantDiagInfo.visible = false
    },


  },

}
</script>

