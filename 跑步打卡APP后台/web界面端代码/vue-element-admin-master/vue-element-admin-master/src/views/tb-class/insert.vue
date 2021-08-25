<template>
  <el-form ref="form"
           :model="form"
           :rules="rules"
           label-width="150px">
    <el-form-item label="班级名称"
                  prop="className">
      <el-input v-model="form.className"></el-input>
    </el-form-item>
    <el-form-item label="班级代码"
                  prop="classCode">
      <el-input v-model="form.classCode"></el-input>
    </el-form-item>

    <el-form-item label="所属学院">
      <el-select v-model="form.collegeId"
                 style="width: 480px;">
        <el-option v-for="item in colleges"
                   :key="item.collegeId"
                   :label="item.collegeName"
                   :value="item.collegeId">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary"
                 @click="onSubmit('form')">立即创建</el-button>
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
  props: ['visible'],
  data () {
    return {
      colleges: [],
      form: {
        className: '',
        classCode: '',
        collegeId: '',
      },
      rules: {
        className: [
          { required: true, message: '班级名称不能为空', trigger: 'blur' },
          { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        ],
        classCode: [
          { required: true, message: '班级代码不能为空', trigger: 'blur' },
          { min: 6, max: 30, message: '长度在 6 到 30 个字符', trigger: 'blur' }
        ],
      },
    }
  },
  created: function () {
    this.getCollegeAll()
  },
  mounted: function () {

  },
  methods: {
    // 查询所有得学院信息
    getCollegeAll () {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      request({
        url: '/api/tb-college/select_all',
        method: 'get',
      }).then(res => {
        let data = res.data
        this.colleges = data
      })
      loading.close()
    },

    // 表单重置函数
    resetForm () {
      this.form.className = ''
      this.form.classCode = ''
      this.form.collegeId = ''
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
            url: '/api/tb-class/insert',
            method: 'post',
            params: {
              className: this.form.className,
              classCode: this.form.classCode,
              collegeId: this.form.collegeId,
            }
          }).then(res => {
            loading.close()
            if (res.code === Global.statusCode['SUCCESS_2000']) {
              this.$message({
                message: '新增' + res.message,
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
          this.$message({
            message: 'error submit!!',
            type: 'error'
          })
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
