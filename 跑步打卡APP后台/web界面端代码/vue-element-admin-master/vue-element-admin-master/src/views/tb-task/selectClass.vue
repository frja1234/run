<template>
  <el-form>
    <el-table ref="multipleTable"
              :data="tableData"
              tooltip-effect="dark"
              style="width: 100%"
              height="350"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection"
                       width="65">
      </el-table-column>
      <el-table-column prop="classCode"
                       label="班级代码"
                       width="180">
      </el-table-column>
      <el-table-column prop="className"
                       label="班级名称"
                       width="280">
      </el-table-column>
      <el-table-column label="所属学院"
                       width="280">
        <template slot-scope="scope">

          <div slot="reference"
               class="name-wrapper">
            <el-tag v-if="typeof(scope.row.college) !== 'undefined'"
                    size="medium">{{ scope.row.college.collegeName }}</el-tag>
            <el-tag v-else
                    type="danger"
                    size="medium">暂无二级学院</el-tag>
          </div>
        </template>
      </el-table-column>

    </el-table>
    <el-button type="primary"
               style="margin-top: 15px; margin-left: 300px"
               @click="onSelect">确认选择</el-button>
    <el-button @click="closeDialog">取消</el-button>
  </el-form>
</template>
<script>
import request from '@/utils/request' // request请求 js
import Global from '../../global/Global'

export default {
  props: ['visible', 'classes'],
  data () {
    return {
      tableData: [],
      multipleSelection: [],
    }
  },
  created: function () {

  },
  mounted: function () {
    let self = this
    const loading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)'
    });
    self.getData(self)
    loading.close();
  },
  methods: {
    // 查询所有的班级信息
    getClassData (self) {
      request({
        url: '/api/tb-class/select_all',
        method: 'get',
      }).then(res => {
        let data = res.data
        self.tableData = data
        console.log(self.tableData, 'self.tableData')
      })
    },

    // 设置默认选中状态
    toggleSelection (self) {
      // let tempClasses = self.classes
      let _this = self
      // console.log(tempClasses, 'tempClasses')
      if (self.classes) {
        _this.$nextTick(() => {
          self.classes.forEach(row => {
            this.$refs.multipleTable.toggleRowSelection(self.tableData.find(item => {
              return row.classId == item.classId;  // 注意这里寻找的字段要唯一，示例仅参考
            }), true);
          });
        })
      } else {
        _this.$refs.multipleTable.clearSelection();
      }
    },
    // 执行getData
    getData (self) {
      // 保证执行完 getClassData() 后 再执行 toggleSelection()
      self.getClassData(self)
      setTimeout(function () {
        self.toggleSelection(self)
      }, 500);

    },

    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    // 关闭窗口
    closeDialog () {
      this.$refs.multipleTable.clearSelection();//清空table的选中
      // 用于修改父页面的 visible属性, 关闭子窗口
      this.$emit('update:visible', false)
    },

    onSelect () {
      let tempClasses = this.multipleSelection
      this.$emit('update:classes', tempClasses)
      // 更新第一个窗口的span
      this.$emit('setShowSelectClass')
      // 关闭窗口
      this.$emit('update:visible', false)
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
