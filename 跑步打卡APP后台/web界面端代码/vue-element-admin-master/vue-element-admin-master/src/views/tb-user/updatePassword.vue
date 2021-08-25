<template>
  <el-form :model="ruleForm"
           status-icon
           :rules="rules"
           ref="ruleForm"
           label-width="100px"
           class="demo-ruleForm"
           style="margin-left: 100px;">
    <el-form-item label="原密码"
                  prop="pass"
                  style="margin-top: 30px;">
      <el-input type="password"
                v-model="ruleForm.pass"
                autocomplete="off"
                style="width: 50%;"></el-input>
    </el-form-item>

    <el-form-item label="新密码"
                  prop="newPass">
      <el-input type="password"
                v-model="ruleForm.newPass"
                autocomplete="off"
                style="width: 50%;"></el-input>
    </el-form-item>

    <el-form-item label="确认密码"
                  prop="checkPass">
      <el-input type="password"
                v-model="ruleForm.checkPass"
                autocomplete="off"
                style="width: 50%;"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary"
                 @click="submitForm('ruleForm')">提交</el-button>
      <el-button @click="resetForm('ruleForm')">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { mapGetters } from 'vuex'
import request from '@/utils/request' // request请求 js
import Global from '../../global/Global'

export default {
  computed: {
    ...mapGetters([
      'userNum'
    ])
  },
  data () {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入原密码'));
      } else if (value.length < 6 || value.length > 15) {
        callback(new Error('请输入6-15位数的密码'));
      } else {
        if (this.ruleForm.newPass !== '') {
          this.$refs.ruleForm.validateField('newPass');
        }
        callback();
      }
    };
    var validateNewPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'));
      } else if (value.length < 6 || value.length > 15) {
        callback(new Error('请输入6-15位数的密码'));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    var validateCheckPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入新密码'));
      } else if (value !== this.ruleForm.newPass) {
        callback(new Error('两次输入的新密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      ruleForm: {
        pass: '',
        newPass: '',
        checkPass: '',
      },
      rules: {
        pass: [
          { validator: validatePass, trigger: 'blur' }
        ],
        newPass: [
          { validator: validateNewPass, trigger: 'blur' }
        ],
        checkPass: [
          { validator: validateCheckPass, trigger: 'blur' }
        ],
      }
    }
  },
  mounted: function () {

  },
  methods: {
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
          request({
            url: '/api/tb-user/update_pass',
            method: 'put',
            params: {
              userNum: this.userNum,
              userPassword: this.ruleForm.pass,
              newPassword: this.ruleForm.newPass,
            }
          }).then(res => {

            if (res.code === Global.statusCode['SUCCESS_2000']) {
              this.$message({
                message: '修改' + res.message,
                type: 'success'
              })
            } else {
              this.$message.error(res.message);
            }
          })
          loading.close()
        } else {
          this.$message({
            type: 'error',
            message: 'error submit!!'
          });
          return false;
        }
      });
    },
    resetForm (formName) {
      this.$refs[formName].resetFields();
    }
  },

}
</script>

