<template>
  <el-form ref="form"
           :model="form"
           label-width="150px">
    <el-form-item label="任务名称">
      &nbsp;&nbsp;
      <span>{{form.taskName}}</span>

    </el-form-item>
    <el-form-item label="要求路程（米）">
      &nbsp;&nbsp;
      <span>{{form.taskRequireDistance}}</span>
    </el-form-item>
    <el-form-item label="要求时间（min）">
      &nbsp;&nbsp;
      <span>{{form.taskRequireTime}}</span>
    </el-form-item>

    <el-form-item label="截止时间">
      &nbsp;&nbsp;
      <span>{{form.taskDeadline}}</span>
      &nbsp;&nbsp;
      <span id="tipTaskDeadlineSpan"
            style="color: red;">截止时间为所展示时间的凌晨0点</span>
    </el-form-item>
    <el-form-item label="完成进度">
      &nbsp;&nbsp;
      <el-progress :text-inside="true"
                   :stroke-width="26"
                   :percentage="setItemProgress(form.data)"
                   v-if="!isNaN(parseInt((form.data.planNum/form.data.completeNum)*100))"
                   :status="setItemStatus(form.data)"
                   :format="setItemText(form.data)"></el-progress>

    </el-form-item>

  </el-form>
</template>
<script>
import request from '@/utils/request' // request请求 js
import Global from '../../global/Global'

export default {
  props: ['visible', 'diagTaskId', 'diagTaskName', 'diagTaskRequireDistance',
    'diagTaskRequireTime', 'diagTaskDeadline', 'diagSelectClasses'],
  data () {

    return {
      form: {
        // 查询的信息
        data: {
          completeNum: '',
          planNum: '',
        },
        // 传入的信息
        taskName: '',
        taskRequireDistance: '',
        taskRequireTime: '',
        taskDeadline: '',
        selectClasses: '',
      }
    }
  },
  created: function () {
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)'
    });
    this.getTaskProcessById() // 加载任务进度信息
    loading.close()
  },
  mounted: function () {

  },
  methods: {

    // 获取日期
    getFormatDate (date) {
      var day3 = new Date(date)
      var s3 = day3.getFullYear() + "-" + (day3.getMonth() + 1) + "-" + day3.getDate()
      return s3
    },
    // 根据任务id获取任务进度的信息
    getTaskProcessById () {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      request({
        url: '/api/tb-task/info_task_process',
        method: 'get',
        params: {
          taskId: this.diagTaskId, // 传入参数
        }
      }).then(res => {
        // console.log(res, 'update_res')
        if (res.code === Global.statusCode['SUCCESS_2000']) {
          let data = res.data
          // 查询的数据
          this.form.data.completeNum = data.ended
          this.form.data.planNum = data.notEnd + data.ended
          // 传入的数据
          this.form.taskName = this.diagTaskName
          this.form.taskRequireDistance = this.diagTaskRequireDistance
          this.form.taskRequireTime = this.diagTaskRequireTime
          this.form.taskDeadline = this.getFormatDate(this.diagTaskDeadline)
          this.form.selectClasses = this.diagSelectClasses
          // 数据处理完成
          loading.close()
          this.$message({
            message: '任务进度信息加载' + res.message,
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



    setItemProgress (data) {
      if (data.completeNum > data.planNum) {
        return 100
      } else {
        return parseInt((data.completeNum / data.planNum).toFixed(1) * 100)
      }
    },

    setItemText (row) {
      return () => {
        return '总人数： ' + row.planNum + '，完成人数： ' + row.completeNum
      }
    },

    setItemStatus (data) {
      if (data.completeNum > data.planNum) {
        return 'exception'
      } else if (data.planNum === data.completeNum) {
        return 'success'
      } else {
        return 'primary'
      }
    }
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
