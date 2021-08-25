<template>
  <el-form ref="form"
           :model="form"
           :rules="rules"
           label-width="150px">
    <el-form-item label="学院名称"
                  prop="collegeName">
      <el-input v-model="form.collegeName"></el-input>
    </el-form-item>
    <el-form-item label="学院代码"
                  prop="collegeCode">
      <el-input v-model="form.collegeCode"></el-input>
    </el-form-item>
    <el-form-item label="学院简介"
                  prop="collegeIntroduction">
      <el-input type="textarea"
                v-model="form.collegeIntroduction"
                autosize></el-input>
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
  props: ['visible', 'diagCollegeId'],
  data () {
    return {
      form: {
        collegeName: '',
        collegeCode: '',
        collegeIntroduction: '',
      },
      rules: {
        collegeName: [
          { required: true, message: '学院名称不能为空', trigger: 'blur' },
          { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        ],
        collegeCode: [
          { required: true, message: '学院代码不能为空', trigger: 'blur' },
          { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        ],
        collegeIntroduction: [
          { min: 0, max: 250, message: '长度在 0 到 250 个字符', trigger: 'blur' }
        ],
      },
    }
  },
  created: function () {
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)'
    });
    this.getCollegeById() // 根据学院id获取学院的信息
    loading.close()
  },
  mounted: function () {

  },
  methods: {
    // 根据学院id获取学院的信息
    getCollegeById () {
      request({
        url: '/api/tb-college/info',
        method: 'get',
        params: {
          collegeId: this.diagCollegeId, // 传入参数
        }
      }).then(res => {
        if (res.code === Global.statusCode['SUCCESS_2000']) {
          let data = res.data
          this.form.collegeName = data.collegeName
          this.form.collegeCode = data.collegeCode
          this.form.collegeIntroduction = data.collegeIntroduction
          this.$message({
            message: '学院信息加载' + res.message,
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
            url: '/api/tb-college/update',
            method: 'put',
            params: {
              collegeId: this.diagCollegeId,
              collegeName: this.form.collegeName,
              collegeCode: this.form.collegeCode,
              collegeIntroduction: this.form.collegeIntroduction,
            }
          }).then(res => {
            loading.close()
            if (res.code === Global.statusCode['SUCCESS_2000']) {
              this.$message({
                message: '修改' + res.message,
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
    },
  },
}
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
