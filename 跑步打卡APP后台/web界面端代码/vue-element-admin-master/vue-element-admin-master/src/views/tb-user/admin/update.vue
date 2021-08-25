<template>
  <el-form ref="form"
           :model="form"
           :rules="rules"
           label-width="100px">
    <el-form-item label="管理员账号"
                  prop="userNum">
      <el-input v-model="form.userNum"></el-input>
    </el-form-item>
    <el-form-item label="管理员姓名"
                  prop="userRealName">
      <el-input v-model="form.userRealName"></el-input>
    </el-form-item>
    <el-form-item label="手机号"
                  prop="userCellPhone">
      <el-input v-model="form.userCellPhone"></el-input>
    </el-form-item>

    <el-form-item label="用户状态">
      <el-select v-model="form.defFlag">
        <el-option v-for="item in form.defFlags"
                   :key="item.value"
                   :label="item.label"
                   :value="item.value">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="身份">
      <el-select v-model="form.role.roleId">
        <el-option :key="form.role.roleId"
                   :label="form.role.roleName+  ' 【'  +form.role.token+ '】'"
                   :value="form.role.roleId">
        </el-option>
      </el-select>
    </el-form-item>

    <el-form-item label="头像">
      <input type="file"
             name="files"
             @change="onClick_uploadimg($event)"
             ref="fileImg"
             accept="image/*">
      <img v-if="form.imageUrl !== '' && typeof(form.imageUrl) !== 'undefined'"
           :src="$IMGURL+form.imageUrl"
           class="avatar">
      <img v-else
           src="../../../icons/pic/no_pic.png"
           class="avatar">
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
import Global from '../../../global/Global'
import { client } from '../configOss.js' // 引入上传图片的js

export default {
  props: ['visible', 'diagUserId'],
  data () {
    // 管理员账号的验证
    var validateUserNum = (rules, value, callback) => {
      if (!value) {
        return callback(new Error('管理员账号号不能为空'));
      }
      // 字母开头，允许6-12字节，允许字母数字下划线: ^[a-zA-Z][a-zA-Z0-9_]{5,11}$
      var pathRegExp = /^[a-zA-Z][a-zA-Z0-9_]{5,11}$/
      if (!pathRegExp.test(value)) {
        return callback(new Error('请输入字母开头，6-12位的管理员账号【允许字母数字下划线】'));
      } else {
        callback();
      }
    };
    // 手机号的正则表达式判断 callback必须是第三个参数
    var validateUserCellPhone = (rules, value, callback) => {
      if (!value) {
        return callback(new Error('手机号不能为空'));
      }
      // 是否为一个路径的正则表达式
      var pathRegExp = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/
      if (!pathRegExp.test(value)) {
        return callback(new Error('不是正确的11位手机号'));
      } else {
        callback();
      }
    };

    return {
      form: {
        userNum: '',
        userRealName: '',
        userCellPhone: '',
        defFlag: '0',
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
        role: {
          roleId: '', // 角色id
          roleName: '', // 角色名
          token: '', // 角色token
        },
        imageUrl: '', // 图片路径
      },
      rules: {
        userNum: [
          { required: true, validator: validateUserNum, trigger: 'blur' },
        ],
        userRealName: [
          { required: true, message: '管理员姓名不能为空', trigger: 'blur' },
          { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
        ],
        userCellPhone: [// ^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$
          { required: true, validator: validateUserCellPhone, trigger: 'blur' },
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
    this.getDataById() // 通过diagUserId来查询出该用户的信息
    loading.close()
  },
  mounted: function () {

  },
  methods: {
    // 通过input的上传测试
    onClick_uploadimg (e) {
      // headIcon/heart-png--3100-thumbnails.png
      var fileName = '/headIcon/' + (new Date().getTime() + 1000) + '.png';
      console.log(fileName, 'fileName')
      let reader = new FileReader();
      let fileData = e.target.files[0];
      console.log(fileData, 'fileData')
      reader.readAsDataURL(fileData);
      client().multipartUpload('/images' + fileName, fileData).then((res) => {
        //var imgUrl = res.res.requestUrls[0];//返回的上传图片的地址
        console.log(res, '成功')
        this.form.imageUrl = fileName
        // e.target.value = "";//input连续上传同一张图片会失效，每次成功把它置空一下
      }).catch((err) => {
        console.log(err, '失败')
      })
    },
    // 通过diagUserId来查询出该用户的信息
    getDataById () {
      request({
        url: '/api/tb-user/info',
        method: 'get',
        params: {
          // 传入参数
          userId: this.diagUserId,
        },
      }).then(res => {
        let data = res.data
        this.form.userNum = data.userNum
        this.form.userRealName = data.userRealName
        this.form.userCellPhone = data.userCellPhone
        this.form.defFlag = data.defFlag + ''
        this.form.teacherId = data.teacher.userId
        this.form.role.roleId = data.role.roleId
        this.form.role.roleName = data.role.roleName
        this.form.role.token = data.role.token
        this.form.imageUrl = data.img
        this.$message({
          message: '用户信息加载' + res.message,
          type: 'success'
        })
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
            url: '/api/tb-user/update',
            method: 'put',
            params: {
              userId: this.diagUserId, // 传入userId，确认修改哪个user
              userNum: this.form.userNum,
              userRealName: this.form.userRealName,
              userCellPhone: this.form.userCellPhone,
              defFlag: this.form.defFlag,
              teacherId: this.form.teacherId,
              img: this.form.imageUrl,
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