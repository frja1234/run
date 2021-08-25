<template>
  <el-form ref="form"
           :model="form"
           label-width="80px">
    <div class="menu"
         id="menu">
      <div class="left"
           id="left">
        <el-table :data="leftTableData"
                  ref="leftTable"
                  style="width: 100%"
                  max-height="550"
                  border
                  stripe>
          <el-table-column prop="userRealName"
                           label="学生姓名"
                           width="200">
          </el-table-column>
          <el-table-column label="操作"
                           width="100">
            <template slot-scope="scope">

              <i @click="addRow(scope.$index, leftTableData,scope.row.userId)"
                 style="cursor:pointer"
                 class="tablei el-icon-circle-plus">
              </i>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="right"
           id="right">
        <el-table :data="rightTableData"
                  ref="rightTable"
                  style="width: 100%"
                  max-height="550"
                  border
                  stripe>

          <el-table-column type="index"
                           label="序号"
                           width="80">
          </el-table-column>

          <el-table-column prop="userRealName"
                           label="学生姓名"
                           width="185">
          </el-table-column>

          <el-table-column prop="userCellPhone"
                           label="学生电话"
                           width="185">
          </el-table-column>
          <el-table-column label="操作"
                           width="100">
            <template slot-scope="scope">

              <i @click="deleteRow(scope.$index, rightTableData,scope.row.userId)"
                 style="cursor:pointer"
                 class="tablei el-icon-delete">
              </i>
            </template>
          </el-table-column>
        </el-table>
      </div>

    </div>
    <el-form-item style="position:absolute; right:15px; bottom:0px;">
      <el-button type="primary"
                 @click="onSubmit()">确认保存</el-button>
      <el-button style="margin-left: 10px"
                 @click="closeDialog">取消</el-button>
    </el-form-item>
  </el-form>
</template>
<script>
import request from '@/utils/request' // request请求 js
import Global from '../../../global/Global'
import { client } from '../configOss.js' // 引入上传图片的js

export default {
  props: ['visible', 'diagUserId'],
  data () {
    return {
      // 左边无教师的学生数据集
      leftTableData: [/* {
        userId: 1,
        userRealName: '小明',
        userCellPhone: '13342525556',
        teacherId: '',
      }, {
        userId: 2,
        userRealName: '小洪',
        userCellPhone: '13342525555',
        teacherId: '',
      }, {
        userId: 3,
        userRealName: '小强',
        userCellPhone: '13342525558',
        teacherId: '',
      } */],
      // 右边教师下的学生表格数据集
      rightTableData: [/* {
        userId: 4,
        userRealName: '小红',
        userCellPhone: '13342525522',
        teacherId: '5',
      }, {
        userId: 5,
        userRealName: '小方',
        userCellPhone: '13342525526',
        teacherId: '5',
      }, {
        userId: 6,
        userRealName: '小丽',
        userCellPhone: '13342525524',
        teacherId: '5',
      } */],
    }
  },
  created: function () {
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)'
    });
    // 获取信息（包括左边的全部没有教师的学生的信息，以及右边的已选学生的信息） 
    this.getAllStudentNoTeacher() // 首先查询出所有没有教师的学生的信息
    this.getStudentListByTeacherId() // 通过diagUserId来查询出该教师下的所选学生的信息
    loading.close()
  },
  mounted: function () {

  },
  methods: {
    // 查询出所有没有教师的学生的信息
    getAllStudentNoTeacher () {
      request({
        url: '/api/tb-user/select_all_student_no_teacher',
        method: 'get',
      }).then(res => {
        let data = res.data
        this.leftTableData = data
      })
    },

    // 通过diagUserId来查询出该教师下的所选学生的信息
    getStudentListByTeacherId () {
      request({
        url: '/api/tb-user/select_all_student_teacher',
        method: 'get',
        params: {
          teacherId: this.diagUserId,
        },
      }).then(res => {
        if (res.code === Global.statusCode['SUCCESS_2000']) {
          let data = res.data
          if (typeof (data) === 'undefined') {
            this.$message({
              message: res.message,
              type: 'warning'
            })
          } else {
            this.$message({
              message: '数据加载' + res.message,
              type: 'success'
            })
            this.rightTableData = data
          }
        } else {
          this.$message({
            message: '数据加载失败, 发生不知名错误',
            type: 'error'
          })
        }
      })
    },

    // 增加按钮 rows = leftTableData
    addRow (index, rows, id) {
      // splice() 方法向/从数组中添加/删除项目，然后返回被删除的项目。
      let obj = rows.splice(index, 1)//从leftTableData中删除数据, 加入到right
      this.rightTableData.push(obj[0]) // 需要加下标，obj[0]，因为obj是数组
    },

    //删除按钮 rows = rightTableData
    deleteRow (index, rows, id) {
      // splice() 方法向/从数组中添加/删除项目，然后返回被删除的项目。
      let obj = rows.splice(index, 1)//从rightTableData中删除数据, 加入到left
      this.leftTableData.push(obj[0])
    },



    // 关闭窗口
    closeDialog () {
      // 用于修改父页面的 visible属性, 关闭子窗口
      this.$emit('update:visible', false)
    },
    // 表单提交函数
    onSubmit () {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      let studentIds = []
      for (let i = 0; i < this.rightTableData.length; i++) {
        // 遍历把右边的userId加入到tempIds数组中
        studentIds.push(this.rightTableData[i].userId)
      }
      request({
        url: '/api/tb-user/choose_students',
        method: 'post',
        params: {
          teacherId: this.diagUserId,
          studentIds: studentIds.toString(),
        },
      }).then(res => {
        loading.close()
        if (res.code === Global.statusCode['SUCCESS_2000']) {
          this.$message({
            message: '操作' + res.message,
            type: 'success'
          })
          // 更新父页面的数据
          this.$emit('getTableData')
          // 用于修改父页面的 visible属性, 关闭子窗口
          this.$emit('update:visible', false)
        } else {
          this.$message.error('操作失败，发生不知名错误');
        }

      })
    },
  },

}
</script>
<style>
.menu {
  width: 800px;
  height: 600px;
}

.left {
  /* float: left; */
  position: absolute;
  left: 0;
  width: 300px;
  height: 500px;
}

.right {
  margin-left: 350px;
  width: 550px;
  height: 500px;
}
</style>