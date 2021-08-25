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
                 @click="selectClass()">选择</el-button>
      &nbsp;
      <span id="showSelectClassSpan"></span>
    </el-form-item>
    <!-- 选择窗口的页面 -->
    <el-dialog :title="SelectDiagInfo.title"
               v-if="SelectDiagInfo.visible"
               :visible.sync="SelectDiagInfo.visible"
               center
               :append-to-body='true'
               :lock-scroll="false"
               width="60%"
               @close="handleCloseSelectDiag">
      <!-- 关闭子窗口执行的函数 -->
      <SelectDiagClass :visible.sync="SelectDiagInfo.visible"
                       :classes.sync="SelectDiagInfo.classes"
                       @setShowSelectClass="setShowSelectClass()" />
    </el-dialog>

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
import SelectDiagClass from './selectClass.vue' // 引入选择班级页面

export default {
  components: {
    SelectDiagClass,
  },
  props: ['visible'],
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
      // 选择班级窗口的信息
      SelectDiagInfo: {
        // 弹出层是否展示， 默认是false， true为展示
        visible: false,
        title: '',
        classes: [],
      },
      form: {
        taskName: this.getTomorrowDate() + '跑步任务',
        taskRequireDistance: '',
        taskRequireTime: '',
        taskDeadline: this.getTomorrowDate(),
        classIds: [],
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

  },
  mounted: function () {

  },
  methods: {

    // 获取明天的日期
    getTomorrowDate () {
      var day3 = new Date()
      day3.setTime(day3.getTime() + 24 * 60 * 60 * 1000)
      var s3 = day3.getFullYear() + "-" + (day3.getMonth() + 1) + "-" + day3.getDate()
      return s3
    },
    // 表单重置函数
    resetForm () {
      this.form.taskName = this.getTomorrowDate() + '跑步任务'
      this.form.taskRequireDistance = ''
      this.form.taskRequireTime = ''
      this.form.taskDeadline = this.getTomorrowDate()
      this.form.classIds = []
      this.SelectDiagInfo.classes = []
      document.getElementById('showSelectClassSpan').innerHTML = ''
      this.form.selectClasses = ''
    },
    // 关闭窗口
    closeDialog () {
      // 用于修改父页面的 visible属性, 关闭子窗口
      this.$emit('update:visible', false)
    },

    // 选择班级
    selectClass () {
      this.SelectDiagInfo.visible = true
      this.SelectDiagInfo.title = '选择班级'
    },

    // 关闭选择子窗口后执行的函数
    handleCloseSelectDiag () {
      this.SelectDiagInfo.visible = false
    },

    // 设置展示选择的班级
    setShowSelectClass () {
      let tempShowClasses = ''
      let tempClassIds = []
      for (let i = 0; i < this.SelectDiagInfo.classes.length; i++) {
        tempClassIds.push(this.SelectDiagInfo.classes[i].classId)
        tempShowClasses = tempShowClasses + this.SelectDiagInfo.classes[i].className + ','
      }
      this.form.classIds = tempClassIds
      document.getElementById('showSelectClassSpan').innerHTML = tempShowClasses
      this.form.selectClasses = tempShowClasses
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
          if (this.form.classIds.length !== 0) {
            const loading = this.$loading({
              lock: true,
              text: 'Loading',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            });
            request({
              url: '/api/tb-task/insert',
              method: 'post',
              params: {
                taskName: this.form.taskName,
                taskRequireDistance: this.form.taskRequireDistance,
                taskRequireTime: this.form.taskRequireTime,
                taskDeadline: this.getFormatDate(this.form.taskDeadline),
                classIds: this.form.classIds.toString(),
                selectClasses: this.form.selectClasses,

              }
            }).then(res => {
              loading.close()
              if (res.code === Global.statusCode['SUCCESS_2000']) {
                this.$message({
                  message: '任务发布' + res.message,
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
              message: '注意：未选择班级！！！',
              type: 'warning'
            })
            document.getElementById('showSelectClassSpan').innerHTML = '注意：未选择班级！！！'
          }


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
