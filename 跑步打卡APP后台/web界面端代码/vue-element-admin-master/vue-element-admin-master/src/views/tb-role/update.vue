<template>
  <el-form ref="form"
           :model="form"
           :rules="rules"
           label-width="80px">
    <el-form-item label="角色代码"
                  prop="roleCode">
      <el-input v-model="form.roleCode"></el-input>
    </el-form-item>
    <el-form-item label="角色名称"
                  prop="roleName">
      <el-input v-model="form.roleName"></el-input>
    </el-form-item>
    <el-form-item label="角色状态">
      <el-select v-model="form.defFlag">
        <el-option v-for="item in form.defFlags"
                   :key="item.value"
                   :label="item.label"
                   :value="item.value">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="角色token">
      <el-select v-model="form.token">
        <el-option v-for="item in form.tokens"
                   :key="item.value"
                   :label="item.label"
                   :value="item.value">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="角色备注"
                  prop="remarks">
      <el-input type="textarea"
                v-model="form.remarks"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary"
                 @click="onSubmit('form')">保存修改</el-button>
      <!-- <el-button>取消</el-button> -->
      <el-button @click="closeDialog">取消</el-button>
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
      form: {
        roleName: '',
        defFlag: '',
        roleCode: '',
        remarks: '',
        token: '', // 角色token
        // 状态（查询选择）
        defFlags: [
          {
            value: '0',
            label: '激活状态'
          }, {
            value: '1',
            label: '注销状态'
          }
        ],
        // tokens（选择）
        tokens: [
          {
            value: 'student',
            label: '学生 student'
          },
          {
            value: 'teacher',
            label: '教师 teacher'
          },
          {
            value: 'admin',
            label: '管理员 admin'
          },
        ],
      },
      rules: {
        roleName: [
          { required: true, message: '角色名称不能为空', trigger: 'blur' },
          { min: 2, max: 15, message: '长度在 2 到 15 个字符', trigger: 'blur' }
        ],
        roleCode: [
          { required: true, message: '角色代码不能为空', trigger: 'blur' },
          { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
        ],
        remarks: [
          { min: 0, max: 255, message: '长度在 255 个字符以内', trigger: 'blur' }
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
    this.getRoleById() // 根据角色id获取角色的信息
    loading.close()
  },
  methods: {
    // 根据角色id获取角色的信息
    getRoleById () {
      request({
        url: '/api/tb-role/info',
        method: 'get',
        params: {
          roleId: this.diagRoleId, // 传入参数
        }
      }).then(res => {
        // console.log(res, 'update_res')
        if (res.code === Global.statusCode['SUCCESS_2000']) {
          let data = res.data
          // console.log(data, 'update_role_data')
          this.form.roleCode = data.roleCode
          this.form.roleName = data.roleName
          this.form.defFlag = data.defFlag + ''
          this.form.token = data.token
          this.form.remarks = data.remarks
          this.$message({
            message: '角色信息加载' + res.message,
            type: 'success'
          })
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
          request({
            url: '/api/tb-role/update',
            method: 'put',
            params: {
              // 参数传递
              roleId: this.diagRoleId,
              roleName: this.form.roleName,
              defFlag: this.form.defFlag,
              roleCode: this.form.roleCode,
              token: this.form.token,
              remarks: this.form.remarks,
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