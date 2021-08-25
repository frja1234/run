<template>

  <el-form ref="form"
           :model="form"
           :rules="rules"
           label-width="80px">
    <el-form-item label="权限名称"
                  prop="permissionName">
      <el-input v-model="form.permissionName"></el-input>
    </el-form-item>
    <el-form-item label="权限状态">
      <el-select v-model="form.defFlag">
        <el-option v-for="item in form.defFlags"
                   :key="item.value"
                   :label="item.label"
                   :value="item.value">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="权限路径"
                  prop="permissionPath">
      <el-input v-model="form.permissionPath"></el-input>
    </el-form-item>
    <el-form-item label="父级权限">
      <!-- <el-input v-model="form.parentPermissionName"
                :readonly="true"></el-input> -->
      <el-select v-model="form.parentPermissionId">
        <!-- v-model绑定的值与option选项value值对应，label值为显示的标签 -->
        <el-option v-for="item in form.parentPermissionList"
                   :key="item.permissionId"
                   :label="item.permissionName+  '     '  +item.permissionPath"
                   :value="item.permissionId">
        </el-option>
      </el-select>
    </el-form-item>

    <el-form-item>
      <el-button type="primary"
                 @click="onSubmit('form')">保存修改</el-button>
      <el-button @click="closeDialog">取消</el-button>
      <!-- <el-button>取消</el-button> -->
      <!-- <el-button type="info"
                 @click="resetForm">重置</el-button> -->
    </el-form-item>
  </el-form>
</template>
<script>
import request from '@/utils/request' // request请求 js
import Global from '../../global/Global'
export default {
  props: ['diagPermissionId', 'visible'],
  data () {
    // 权限路径的正则表达式判断
    var checkPermissionPath = (rules, value, callback) => {
      if (!value) {
        return callback(new Error('权限路径不能为空'));
      }
      // 是否为一个路径的正则表达式
      var pathRegExp = /^(\/)[^\s]+/
      if (!pathRegExp.test(value)) {
        return callback(new Error('权限路径格式出错, 正确格式为: /test/child-test'));
      } else {
        callback();
      }
    };
    return {
      form: {
        permissionName: '',
        defFlag: '',
        permissionPath: '',
        parentPermissionId: null,
        parentPermissionName: null,
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
        // select下拉框父级节点，必须激活状态，且不包括自己
        parentPermissionList: [
          {
            permissionId: '',
            permissionName: '无父级节点',
            permissionPath: '无'
          }
        ],
      },
      rules: {
        permissionName: [
          { required: true, message: '权限名称不能为空', trigger: 'blur' },
          { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        ],
        permissionPath: [
          { required: true, validator: checkPermissionPath, trigger: 'blur' }
        ],
      },
    }
  },
  mounted: function () {
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)'
    });
    this.getPermissionById() // 根据权限id获取权限的信息(id, name, path, 父级权限)
    this.getPermissionList() // 获取所有权限的信息（id和name）
    loading.close()
  },
  methods: {
    // 根据权限id获取权限的信息(id, name, path, 父级权限)
    getPermissionById () {
      request({
        url: '/api/tb-permission/info',
        method: 'get',
        params: {
          permissionId: this.diagPermissionId, // 传入参数
        }
      }).then(res => {

        if (res.code === Global.statusCode['SUCCESS_2000']) {
          let data = res.data
          this.form.permissionName = data.permissionName
          this.form.permissionPath = data.permissionPath
          this.form.defFlag = data.defFlag + ''
          // 说明该节点没有父节点
          if (typeof (data.parentPermissionId) === "undefined") {
            this.form.parentPermissionId = ''
            this.form.parentPermissionName = '无父级权限'
          } else {
            this.form.parentPermissionId = data.parentPermissionId
            this.form.parentPermissionName = data.parentPermission.permissionName
          }
          this.$message({
            message: '权限信息加载' + res.message,
            type: 'success'
          })
        } else {
          this.$message.error(res.message);
        }
      })
    },
    // 获取所有权限的信息（id和name）
    getPermissionList () {
      // 用于修改权限的父级节点，不能包括自己
      request({
        url: '/api/tb-permission/select',
        method: 'get',
        params: {
          defFlag: '0', // 只能够是那种激活状态的，才能当父级节点
          size: 1000,
          current: 1,
        }
      }).then(res => {
        console.log(res, 'update_select_listPermission')
        if (res.code === Global.statusCode['SUCCESS_2000']) {
          let arrList = res.data.records
          for (let i = 0; i < arrList.length; i++) {
            // 不是自己 才能够加入到父级节点的下拉框当中
            if (arrList[i].permissionId !== this.diagPermissionId) {
              let obj = {
                permissionId: arrList[i].permissionId,
                permissionName: arrList[i].permissionName,
                permissionPath: arrList[i].permissionPath,
              }
              this.form.parentPermissionList.push(obj)
            }
          }

        } else {
          this.$message.error(res.message);
        }
      })
    },

    // 关闭窗口
    closeDialog () {
      // 用于修改父页面的 visible属性, 关闭子窗口
      this.$emit('update:visible', false)
    },

    // 表单提交函数
    onSubmit (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
          // 新增
          request({
            url: '/api/tb-permission/update',
            method: 'put',
            params: {
              // 参数传递
              permissionId: this.diagPermissionId,
              permissionName: this.form.permissionName,
              defFlag: this.form.defFlag,
              permissionPath: this.form.permissionPath,
              parentPermissionId: this.form.parentPermissionId,
            }
          }).then(res => {
            loading.close()
            if (res.code === Global.statusCode['SUCCESS_2000']) {
              this.$message({
                message: res.message,
                type: 'success'
              })
              // 更新父页面的数据
              this.$emit('getTableData')
              // 用于修改父页面的 visible属性, 关闭子窗口
              this.$emit('update:visible', false)
            } else {
              this.$message.error('修改失败, 发生不知名错误');
            }
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    }
  },

}
</script>