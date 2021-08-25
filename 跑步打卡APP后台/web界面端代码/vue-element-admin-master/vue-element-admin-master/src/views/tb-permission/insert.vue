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
      <el-select v-model="form.parentPermissionId"
                 style="width: 100%;">
        <el-option :key="form.parentPermissionId"
                   :label="form.parentPermissionName+  '     '  +form.parentPermissionPath"
                   :value="form.parentPermissionId">
        </el-option>
      </el-select>
    </el-form-item>

    <el-form-item>
      <el-button type="primary"
                 @click="onSubmit('form')">立即创建</el-button>
      <!-- <el-button>取消</el-button> -->
      <el-button type="info"
                 @click="resetForm">重置</el-button>
      <el-button @click="closeDialog">取消</el-button>
    </el-form-item>
  </el-form>
</template>
<script>
import request from '@/utils/request' // request请求 js
import Global from '../../global/Global'
export default {
  props: ['diagParentPermissionId', 'diagParentPermissionName', 'diagParentPermissionPath', 'visible'],
  data () {
    // 权限路径的正则表达式判断 callback必须是第三个参数
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
        defFlag: '0',
        permissionPath: '',
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
        parentPermissionId: this.diagParentPermissionId,
        parentPermissionName: this.diagParentPermissionName,
        parentPermissionPath: this.diagParentPermissionPath,
        /* parentPermission: [{
          permission_id: 0,
          permission_name: '无父级权限'
        }], */
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

  methods: {

    // 表单重置函数
    resetForm () {
      // this.$refs[formName].resetFields();
      this.form.permissionName = ''
      this.form.defFlag = '0'
      this.form.permissionPath = ''
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
          request({
            url: '/api/tb-permission/insert',
            method: 'post',
            params: {
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
              this.$message.error(res.message);
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