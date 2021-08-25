<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="query.permissionName"
                placeholder="权限名称"
                style="width: 200px;"
                class="filter-item" />

      <el-select v-model="query.defFlag"
                 placeholder="权限状态"
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
        新增父级权限
      </el-button>
      <el-button class="filter-item"
                 style="margin-left: 10px;"
                 type="info"
                 icon="el-icon-refresh-left"
                 @click="handleReSet">
        重置查询条件
      </el-button>
      <!-- <el-button class="filter-item"
                 style="margin-left: 10px;"
                 type="info"
                 icon="el-icon-refresh-left"
                 @click="handleConsole">
        打印数据
      </el-button> -->
    </div>
    <!-- 新增窗口的页面 -->
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
      <InsertDiagPermission :diagParentPermissionId="InsertDiagInfo.diag_parent_permission_id"
                            :diagParentPermissionName="InsertDiagInfo.diag_parent_permission_name"
                            :diagParentPermissionPath="InsertDiagInfo.diag_parent_permission_path"
                            :visible.sync="InsertDiagInfo.visible"
                            @getTableData="getTableData">
        <!-- :visible.sync双向绑定数据
              @getTableData中的getTableData是子窗口的函数, "getTableData"是父窗口的函数,
              绑定起来, 子窗口通过this.$emit('getTableData') 来调用父窗口的getTableData函数,
              刷新页面 -->
      </InsertDiagPermission>
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
      <UpdateDiagPermission :diagPermissionId="UpdateDiagInfo.diag_permission_id"
                            :visible.sync="UpdateDiagInfo.visible"
                            @getTableData="getTableData">
        <!-- :visible.sync双向绑定数据 -->
      </UpdateDiagPermission>
    </el-dialog>
    <!-- 分类表格
:data(设置数据源) :columns(设置表格中列配置信息) :selection-type(是否有复选框)
:expand-type(是否展开数据) show-index(是否设置索引列) index-text(设置索引列头)
border(是否添加纵向边框) :show-row-hover(是否鼠标悬停高亮) stripe(斑马纹) -->
    <tree-table :data="tableData"
                :columns="columns"
                :expand-type="false"
                :show-row-hover="true"
                :selection-type="false"
                show-index
                index-text="序号"
                border>

      <template slot="permission_name_slot"
                slot-scope="scope">
        <span>{{ scope.row.permission_name }}</span>

      </template>

      <template slot="def_flag_slot"
                slot-scope="scope">
        <i class="el-icon-success"
           v-if="!scope.row.def_flag"
           style="color: lightGreen"></i>
        <i class="el-icon-error"
           v-else
           style="color: red"></i>
      </template>
      <template slot="permission_path_slot"
                slot-scope="scope">

        <div slot="reference"
             class="name-wrapper">
          <el-tag size="medium">{{ scope.row.permission_path }}</el-tag>
        </div>
      </template>
      <template slot="opt_slot"
                slot-scope="scope">
        <el-button size="mini"
                   v-if="!scope.row.def_flag"
                   type="info"
                   plain
                   @click="handleCancel(scope.row.permission_id)">注销</el-button>
        <el-button size="mini"
                   v-else
                   type="warning"
                   plain
                   @click="handleActivate(scope.row.permission_id)">激活</el-button>
        <el-button size="mini"
                   type="primary"
                   plain
                   @click="handleInsert(scope.row.permission_id, scope.row.permission_name, scope.row.permission_path)">新增子级权限</el-button>
        <el-button size="mini"
                   type="success"
                   plain
                   @click="handleUpdate(scope.row.permission_id)">编辑</el-button>
        <el-button size="mini"
                   type="danger"
                   plain
                   @click="handleDelete(scope.row.permission_id, scope.row.permission_name, scope.row.permission_path)">删除</el-button>
      </template>

    </tree-table>
  </div>
</template>

<script>
import request from '@/utils/request' // request请求 js
import Global from '../../global/Global'
import InsertDiagPermission from './insert.vue' // 引入新增权限页面
import UpdateDiagPermission from './update.vue' // 引入修改权限页面

export default {
  components: {
    InsertDiagPermission,
    UpdateDiagPermission
  },
  data () {
    return {
      // 插入窗口的信息
      InsertDiagInfo: {
        diag_parent_permission_id: null,
        diag_parent_permission_name: null,
        diag_parent_permission_path: null,
        // 弹出层是否展示， 默认是false， true为展示
        visible: false,
        title: '',
      },
      // 修改窗口的信息
      UpdateDiagInfo: {
        diag_permission_id: -1, // 传入当前权限的id，用于修改子窗口重新查询信息
        // 弹出层是否展示， 默认是false， true为展示
        visible: false,
        title: '',
      },
      // 数据列
      columns: [

        /* {
          label: '序号',
          prop: 'permission_id',
          type: 'template',
          template: 'permission_id_slot',
          width: 150
        }, */
        {
          label: '权限名称',
          prop: 'permission_name',
          type: 'template',
          template: 'permission_name_slot',
          width: 240
        },
        {
          label: '权限路径',
          prop: 'permission_path',
          type: 'template',
          template: 'permission_path_slot'
        }, {
          label: '权限状态',
          prop: 'def_flag',
          type: 'template',
          template: 'def_flag_slot',
          width: 100
        }, {
          label: '操作',
          type: 'template',
          template: 'opt_slot'
        }
      ],
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
        permissionName: '',
        size: 500,
        current: 1
      },
    }
  },
  mounted: function () {
    this.getTableData() //需要触发的函数
  },
  methods: {
    // 处理父子数据关系的函数
    toTreeData (data) {
      let resData = data;
      // console.log(typeof (resData[0].parentPermissionId) === "undefined", 'resData[0].parentPermissionId')
      let tree = [];
      // 处理父节点
      for (let i = 0; i < resData.length; i++) {
        // 如果parentPermission中的permissionId 为null 或者 undefined，则说明此节点为父节点
        if (typeof (resData[i].parentPermissionId) === "undefined") {
          // 注意这里的parentPosition 要和自己的父节点id字段一样
          let obj = {
            permission_id: resData[i].permissionId, // permissionId是为了排序的
            permission_name: resData[i].permissionName,
            permission_path: resData[i].permissionPath,
            def_flag: resData[i].defFlag,
            children: []
          };
          tree.push(obj);
          resData.splice(i, 1);
          i--;
        }
      }
      run(tree);
      // 处理子节点
      function run (chiArr) {
        // console.log(resData.length, 'resData length print')
        if (resData.length !== 0) {
          for (let i = 0; i < chiArr.length; i++) {
            // console.log(chiArr[i].permission_id, 'chiArr[' + i + '].permission_id')
            for (let j = 0; j < resData.length; j++) {
              // chiArr[i].permissionId 是父节点的id
              // console.log(resData[j].parentPermissionId, 'resData[' + j + '].parentPermissionId')
              if (chiArr[i].permission_id === resData[j].parentPermissionId) {
                let obj = {
                  permission_id: resData[j].permissionId, // permissionId是为了排序的
                  permission_name: resData[j].permissionName,
                  permission_path: resData[j].permissionPath,
                  def_flag: resData[j].defFlag,
                  children: []
                };
                chiArr[i].children.push(obj);
                resData.splice(j, 1);
                j--;
              }
            }
            // 递归，求子节点的子节点...
            run(chiArr[i].children);
          }
        }
      }
      // 返回最后的父节点数组
      return tree;
    },
    /* 
        获取 包装数据，因为搜索后分页数据会变化，
        所以这里可以传值到后台判断是否进行了搜索展示，
        这里只做简单展示，不考虑搜索和分页
    */
    getTableData () {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      request({
        url: '/api/tb-permission/select',
        method: 'get',
        params: {
          size: this.query.size,
          current: this.query.current,
          permissionName: this.query.permissionName,
          defFlag: this.query.defFlag
        }
      }).then(res => {
        // console.log(res.data.total, 'permissionList_index_res_data.records')
        // console.log(res.data.records, 'permissionList_index_res_data.records')
        let arr_list = this.toTreeData(res.data.records)
        // console.log(arr_list, 'res_arr_list')
        this.tableData = arr_list
      })
      loading.close()
    },

    // 处理查询
    handleQuery () {
      this.getTableData()
    },

    // 处理重置查询条件
    handleReSet () {
      this.query.permissionName = ''
      this.query.defFlag = ''
    },

    // 处理注销操作
    handleCancel (permissionId) {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      request({
        url: '/api/tb-permission/cancel',
        method: 'put',
        params: {
          // 参数传递
          permissionId: permissionId,
        }
      }).then(res => {
        if (res.code === Global.statusCode['SUCCESS_2000']) {
          this.$message({
            message: '权限注销' + res.message,
            type: 'success'
          })
          // 刷新页面
          this.getTableData()
        } else {
          this.$message.error('权限注销失败, 发生不知名错误');
        }
      })
      loading.close()
    },

    // 处理激活操作
    handleActivate (permissionId) {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      request({
        url: '/api/tb-permission/activate',
        method: 'put',
        params: {
          // 参数传递
          permissionId: permissionId,
        }
      }).then(res => {
        if (res.code === Global.statusCode['SUCCESS_2000']) {
          this.$message({
            message: '权限激活' + res.message,
            type: 'success'
          })
          // 刷新页面
          this.getTableData()
        } else {
          this.$message.error('权限激活失败, 发生不知名错误');
        }
      })
      loading.close()
    },

    // 处理插入操作
    handleInsert (permissionId, permissionName, permissionPath) {
      // console.log(permissionId, 'permissionId')

      // 赋值传给子窗口
      this.InsertDiagInfo.diag_parent_permission_id = permissionId
      this.InsertDiagInfo.diag_parent_permission_name = permissionName
      this.InsertDiagInfo.diag_parent_permission_path = permissionPath

      this.InsertDiagInfo.title = '新增权限' // 设置题目
      this.InsertDiagInfo.visible = true; // 默认页面不显示为false,点击按钮将这个属性变成true
    },

    // 处理修改操作
    handleUpdate (permissionId) {
      this.UpdateDiagInfo.diag_permission_id = permissionId // 把当前的权限id传入到修改子窗口
      this.UpdateDiagInfo.title = '编辑权限' // 设置题目
      this.UpdateDiagInfo.visible = true; // 默认页面不显示为false,点击按钮将这个属性变成true
    },

    // 处理删除操作, 不可逆操作, 注意！！！！！！
    handleDelete (permissionId, permissionName, permissionPath) {
      this.$confirm('此操作为不可逆操作，将永久删除权限：' + permissionName + '     ' + permissionPath + ' 及其子级权限, 是否继续?', '警告', {
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
          url: '/api/tb-permission/delete',
          method: 'delete',
          params: {
            // 参数传递
            permissionId: permissionId,
          }
        }).then(res => {
          loading.close()
          if (res.code === Global.statusCode['SUCCESS_2000']) {
            this.$message({
              message: '权限删除' + res.message,
              type: 'success'
            })
            // 刷新页面
            this.getTableData()
          } else {
            this.$message.error('权限删除失败, 发生不知名错误');
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除操作'
        });
      });
    },

    // 处理父级权限的新增操作
    handleCreate () {
      this.handleInsert(null, '无父级权限', '无')
    },

    // 关闭新增子窗口后执行的函数
    handleCloseInsertDiag () {
      this.InsertDiagInfo.visible = false
    },

    // 关闭修改子窗口后执行的函数
    handleCloseUpdateDiag () {
      this.UpdateDiagInfo.visible = false
    },


  },

}
</script>