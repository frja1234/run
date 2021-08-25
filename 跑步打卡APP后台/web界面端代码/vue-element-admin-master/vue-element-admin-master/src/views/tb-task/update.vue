<template>
  <el-form ref="form"
           :model="form"
           :rules="rules"
           label-width="150px">
    <el-form-item label="任务名称"
                  prop="taskName">
      <el-input v-model="form.taskName"></el-input>
    </el-form-item>
    <el-form-item label="要求路程（米）"
                  prop="taskRequireDistance">
      <el-input v-model="form.taskRequireDistance"></el-input>
    </el-form-item>
    <el-form-item label="要求时间（min）"
                  prop="taskRequireTime">
      <el-input v-model="form.taskRequireTime"></el-input>
    </el-form-item>

    <el-form-item label="截止时间"
                  prop="taskDeadline">
      <el-date-picker v-model="form.taskDeadline"
                      type="date"
                      placeholder="选择截至时间">
      </el-date-picker>
      &nbsp;
      <span id="tipTaskDeadlineSpan"
            style="color: red;">截止时间为选择时间的凌晨0点，比如8-12，则任务截止时间为8-12号的0点，发布的新任务默认截止时间是次日0点</span>
    </el-form-item>
    <el-form-item label="选择班级">
      <el-button type="success"
                 disabled>选择</el-button>
      &nbsp;
      <span id="showSelectClassSpan"></span>
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
  props: ['visible', 'diagTaskId'],
  data () {
    // 要求路程的验证
    var validateTaskRequireDistance = (rules, value, callback) => {
      if (!value) {
        return callback(new Error('要求路程不能为空'));
      }
      if (value > 0 && value < 1000000) {
        callback();
      } else {
        return callback(new Error('请输入1-999999米的要求路程'));
      }
    };
    // 要求时间的验证
    var validateTaskRequireTime = (rules, value, callback) => {
      if (!value) {
        return callback(new Error('要求时间不能为空'));
      }
      if (value > 0 && value < 181) {
        callback();
      } else {
        return callback(new Error('请输入1-180分钟的要求时间'));
      }
    };
    return {
      form: {
        taskName: '',
        taskRequireDistance: '',
        taskRequireTime: '',
        taskDeadline: '',
        selectClasses: '',
      },
      rules: {
        taskName: [
          { required: true, message: '任务名称不能为空', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        taskRequireDistance: [
          { required: true, validator: validateTaskRequireDistance, trigger: 'blur' },
        ],
        taskRequireTime: [
          { required: true, validator: validateTaskRequireTime, trigger: 'blur' },
        ],
        taskDeadline: [
          { required: true, message: '截止时间不能为空', trigger: 'blur' },
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
    this.getTaskById() // 加载任务信息
    loading.close()
  },
  mounted: function () {

  },
  methods: {
    // 根据任务id获取任务的信息
    getTaskById () {
      request({
        url: '/api/tb-task/info',
        method: 'get',
        params: {
          taskId: this.diagTaskId, // 传入参数
        }
      }).then(res => {
        if (res.code === Global.statusCode['SUCCESS_2000']) {
          let data = res.data
          this.form.taskName = data.taskName
          this.form.taskRequireDistance = data.taskRequireDistance
          this.form.taskRequireTime = data.taskRequireTime
          this.form.taskDeadline = this.getFormatDate(data.taskDeadline)
          if (typeof (data.selectClasses) !== 'undefined') {
            this.form.selectClasses = data.selectClasses
            document.getElementById('showSelectClassSpan').innerHTML = this.form.selectClasses
          } else {
            document.getElementById('showSelectClassSpan').innerHTML = '注意：未选择班级！！！'
          }
          this.$message({
            message: '任务信息加载' + res.message,
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

    // 获取日期
    getFormatDate (date) {
      var day3 = new Date(date)
      var s3 = day3.getFullYear() + "-" + (day3.getMonth() + 1) + "-" + day3.getDate()
      return s3
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
            url: '/api/tb-task/update',
            method: 'put',
            params: {
              taskId: this.diagTaskId,
              taskName: this.form.taskName,
              taskRequireDistance: this.form.taskRequireDistance,
              taskRequireTime: this.form.taskRequireTime,
              taskDeadline: this.getFormatDate(this.form.taskDeadline),
            }
          }).then(res => {
            loading.close()
            if (res.code === Global.statusCode['SUCCESS_2000']) {
              this.$message({
                message: '任务修改' + res.message,
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
