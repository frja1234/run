<template>
  <el-form ref="form"
           :model="form"
           label-width="80px">
    <div class="menu"
         id="menu">
      <div class="left"
           id="left">
        <!-- v-resize="resize" -->
        <el-tree class="treeitems"
                 :data="leftTableData"
                 show-checkbox
                 node-key="id"
                 :props="defaultProps"
                 :default-checked-keys="permissionIdList"
                 @check-change="handleCheck"
                 default-expand-all
                 ref="tree">
          <!-- @check-change="handleCheck"
        @node-click="handleNodeClick" -->
        </el-tree>
      </div>
      <div class="right"
           id="right">
        <el-table :data="rightTableData"
                  style="width: 100%"
                  max-height="650"
                  border
                  stripe>

          <el-table-column type="index"
                           label="序号"
                           width="80">
          </el-table-column>

          <el-table-column prop="label"
                           label="权限名称"
                           width="150">
          </el-table-column>

          <el-table-column prop="permission_path"
                           label="权限路径"
                           width="220">
            <template slot-scope="scope">
              <el-tag :type="success"
                      disable-transitions>{{scope.row.permission_path}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作"
                           width="100">
            <template slot-scope="scope">

              <i @click="deleteRow(scope.$index, rightTableData,scope.row.id)"
                 style="cursor:pointer"
                 class="tablei el-icon-delete">
              </i>
            </template>
          </el-table-column>
        </el-table>
      </div>

    </div>
    <el-form-item style="position:absolute; right:15px; bottom:0px;">
      <el-button type="primary"
                 @click="onSubmit()">确认授权</el-button>
      <el-button style="margin-left: 10px"
                 @click="closeDialog">取消</el-button>
    </el-form-item>
  </el-form>
</template>
<script>
import request from '@/utils/request' // request请求 js
import Global from '../../global/Global'

export default {
  props: ['diagRoleId', 'visible'],
  data () {
    return {
      // dataText: '没有数据哦',
      // 权限的信息集
      tableData: [],
      // 左边展示的选中的权限信息
      leftTableData: [],
      // 右边展示的选中的权限信息
      rightTableData: [],
      // 复选框默认选中哪些
      permissionIdList: [],
      defaultProps: {
        children: 'children',
        label: 'label',
        disabled: 'disabled',
      },


      // 查询对象
      query: {
        size: 500,
        current: 1
      },
    }
  },
  // 局部注册指令
  directives: {
    resize: { // 指令的名称
      bind (el, binding) { // el为绑定的元素，binding为绑定给指令的对象
        let width = '', height = '';
        function isReize () {
          const style = document.defaultView.getComputedStyle(el);
          if (width !== style.width || height !== style.height) {
            binding.value(style.width, style.height);  // 关键 绑定函数
          }
          width = style.width;
          height = style.height;

        }
        el.__vueSetInterval__ = setInterval(isReize, 300);
      },
      unbind (el) {
        clearInterval(el.__vueSetInterval__);
      }
    }

  },
  // 可以获取数据，但还没渲染dom组件
  created: function () {
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)'
    });
    let self = this
    self.getData(self)
    loading.close()
  },
  // 已经渲染了dom了
  mounted: function () {
    // 设置选中状态
    this.$nextTick(() => {
      this.$refs.tree.setCheckedKeys(this.permissionIdList, true)//默认选中状态
    })
  },
  methods: {
    // 生成tableData数据集的方法
    toTableData (data) {
      let tempData = data
      let tree = []
      for (let i = 0; i < tempData.length; i++) {
        // 不是父节点才加入到tableData中去
        if (typeof (tempData[i].parentPermissionId) !== "undefined") {
          // console.log(tempData[i], 'tempData[' + i + ']')
          let obj = {
            permissionId: tempData[i].permissionId, // permissionId是为了排序的
            permissionName: tempData[i].permissionName,
            permissionPath: tempData[i].permissionPath,
            defFlag: tempData[i].defFlag,
          }
          tree.push(obj)
        }
      }
      return tree
    },

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
            id: resData[i].permissionId, // permissionId是为了排序的
            label: resData[i].permissionName,
            permission_path: resData[i].permissionPath,
            disabled: resData[i].defFlag === 0 ? false : true,
            children: [],
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
              if (chiArr[i].id === resData[j].parentPermissionId) {
                let obj = {
                  id: resData[j].permissionId, // permissionId是为了排序的
                  label: resData[j].permissionName,
                  permission_path: resData[j].permissionPath,
                  disabled: resData[j].defFlag === 0 ? false : true,
                  children: [],
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
    // 组装 rightTableData 的数据集，从 tableData 中 根据permissionIdList筛选
    packPermissionIdList (self) {
      // 创建一个临时的数据集，用于遍历删除
      let tempTableData = self.tableData
      let tempPermissionIdList = self.permissionIdList
      let arrList = []
      for (let i = 0; i < tempPermissionIdList.length; i++) {
        for (let j = 0; j < tempTableData.length; j++) {
          // 如果tableData的id是===permissionIdList[i], 说明有该权限

          if (tempTableData[j].permissionId === tempPermissionIdList[i]) {
            let obj = {
              id: tempTableData[j].permissionId, // permissionId是为了排序的
              label: tempTableData[j].permissionName,
              permission_path: tempTableData[j].permissionPath,
              disabled: tempTableData[j].defFlag === 0 ? false : true,
              children: [],
            }

            arrList.push(obj)
            tempTableData.splice(j, 1)
            break
          }
        }
      }
      self.rightTableData = arrList
      console.log(this.rightTableData, 'this.rightTableData')
      console.log(this.leftTableData, 'this.leftTableData')

    },

    // 查询权限信息集
    getTableData (self) {
      return request({
        url: '/api/tb-permission/select',
        method: 'get',
        params: {
          size: self.query.size,
          current: self.query.current,
        }
      }).then(res => {
        // 最好都用临时变量存储，感觉是引用的问题, splice会永久切掉一切
        let arr = res.data.records
        let arr_list = self.toTableData(arr)
        self.tableData = arr_list
        // console.log(this.tableData, 'this.tableData')
        // console.log(arr, 'arr')
        let arr_tree_list = self.toTreeData(arr)

        // 左边展示的树形数据
        self.leftTableData = arr_tree_list

      })
    },



    // 根据roleId获取该角色的权限的id集合
    getPermissionIdList (self) {
      request({
        url: '/api/tb-role/info_permission',
        method: 'get',
        params: {
          roleId: self.diagRoleId,
        }
      }).then(res => {
        if (res.code === Global.statusCode['SUCCESS_2000']) {
          if (typeof (res.data) === 'undefined') {
            self.$message({
              message: res.message,
              type: 'warning'
            })
          } else {
            self.$message({
              message: '加载' + res.message,
              type: 'success'
            })
            // 赋值给permissionIdList
            self.permissionIdList = res.data
            // 设置 rightTableData 的数据集，从 tableData 中 根据permissionIdList筛选
            self.packPermissionIdList(self)

          }
        }
      })
    },

    // 结合 getTableData() 以及 getPermissionIdList() 保证这2个函数同步执行
    getData (self) {
      // 保证执行完 getTableData() 后 再执行 getPermissionIdList()
      self.getTableData(self).then(() => {
        // console.log(val, 'val')
        self.getPermissionIdList(self)
      })

    },

    //复选框点击事件
    handleCheck (data, change) {
      let nodes = this.$refs.tree.getCheckedNodes().concat(this.$refs.tree.getHalfCheckedNodes())
      console.log(nodes, 'nodes')
      let filterNodes = nodes.filter(node => { // 过滤出子节点，也就是不含childred字段的节点数据
        return node.children.length === 0 // children.length === 0说明是子节点
      })
      console.log(filterNodes, 'filterNodes')
      this.rightTableData = filterNodes;//赋值给表格数据
    },

    //删除按钮
    deleteRow (index, rows, id) {
      this.$refs.tree.setChecked(id, false);//取消左侧复选框的选中状态
      rows.splice(index, 1)//从rightTableData中删除数据
    },

    // 关闭窗口
    closeDialog () {
      // 用于修改父页面的 visible属性, 关闭子窗口
      this.$emit('update:visible', false)
    },


    // 提交表单
    onSubmit () {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      // 最终选中哪些
      let permissionIds = []
      for (let i = 0; i < this.rightTableData.length; i++) {
        permissionIds.push(this.rightTableData[i].id)
      }
      // 进行post请求，grant授权操作
      request({
        url: '/api/tb-role/grant',
        method: 'POST',
        params: {
          // 角色id
          roleId: this.diagRoleId,
          // 需要授予权限的ids
          permissionIds: permissionIds.toString(),
        }
      }).then(res => {
        loading.close()
        if (res.code === Global.statusCode['SUCCESS_2000']) {
          this.$message({
            message: '角色授权' + res.message,
            type: 'success'
          })
          // 更新父页面的数据
          this.$emit('getTableData')
          // 用于修改父页面的 visible属性, 关闭子窗口
          this.$emit('update:visible', false)
        } else {
          this.$message.error('角色授权失败，发生不知名错误');
        }
      })
    },
    // 指令绑定函数
    resize (width, height) {
      console.log(width, height)
      document.getElementById('menu').style.height = height
    }
  },
}
</script>

<style>
.menu {
  width: 800px;
  height: 620px;
}

.left {
  /* float: left; */
  position: absolute;
  left: 0;
  width: 300px;
}

.right {
  margin-left: 350px;
  width: 550px;
}
</style>
