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
                 @click="onSubmit('form')">保存修改</el-button>
      <el-button @click="closeDialog">取消</el-button>
    </el-form-item>
  </el-form>
</template>
<script>
import request from '@/utils/request' // request请求 js
import Global from '../../global/Global'

export default {
  props: ['visible', 'diagClassId'],
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
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)'
    });
    this.getCollegeAll()
    this.getClassById()
    loading.close()
  },
  mounted: function () {

  },
  methods: {

    // 根据班级id获取班级的信息
    getClassById () {

      request({
        url: '/api/tb-class/info',
        method: 'get',
        params: {
          classId: this.diagClassId, // 传入参数
        }
      }).then(res => {
        if (res.code === Global.statusCode['SUCCESS_2000']) {
          let data = res.data
          this.form.className = data.className
          this.form.classCode = data.classCode
          this.form.collegeId = data.collegeId
          this.$message({
            message: '班级信息加载' + res.message,
            type: 'success'
          })
        } else {
          this.$message.error(res.message);
        }
      })

    },
    // 查询所有得学院信息
    getCollegeAll () {
      request({
        url: '/api/tb-college/select_all',
        method: 'get',
      }).then(res => {
        let data = res.data
        this.colleges = data
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
            url: '/api/tb-class/update',
            method: 'put',
            params: {
              classId: this.diagClassId,
              className: this.form.className,
              classCode: this.form.classCode,
              collegeId: this.form.collegeId,
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
