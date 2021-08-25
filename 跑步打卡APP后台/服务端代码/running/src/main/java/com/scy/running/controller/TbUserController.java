package com.scy.running.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scy.running.conf.ResultInfo;
import com.scy.running.conf.Status;
import com.scy.running.model.TbPermission;
import com.scy.running.model.TbPunchClock;
import com.scy.running.model.TbRole;
import com.scy.running.model.TbUser;
import com.scy.running.page.MyVo;
import com.scy.running.service.ITbUserService;
import com.scy.running.utils.HttpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * InnoDB free: 11264 kB; (`role_id`) REFER `scy_running_app/tb_role`(`role_id`) 前端控制器
 * </p>
 *
 * @author scy
 * @since 2021-07-09
 */
@RestController
@RequestMapping("/api/tb-user")
@Api(tags = "用户管理的接口")
public class TbUserController {

    @Autowired
    private ITbUserService userService;


    /**
     * 用户登录
     * @param request
     * @param response
     * @param user
     * @throws IOException
     */
    @ApiOperation("用户登录的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户登录_用户名和密码接收对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public void loginUser(HttpServletRequest request, HttpServletResponse response, TbUser user) throws IOException {
        TbUser tbUser = userService.loginUserAndRoleTokenByObj(user);
        TbUser updateUser = new TbUser();
        updateUser.setUserId(tbUser.getUserId());
        updateUser.setLastLoginTime(new Date());
        updateUser.setLastLoginIp(HttpUtils.getIpAddress(request));
        userService.updateUserByObj(updateUser);
        Map<String,Object> map= new HashMap<>();
        map.put("params",user);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        if (tbUser.getUserId() == null){
            // 根据手机号和密码找不到相应的用户
            result = new ResultInfo(Status.LOGIN_ERROR);
            result.setData(null);
        } else{
            // 登录成功
            result = new ResultInfo(Status.SUCCESS);
            result.setData(tbUser);
        }
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 用户登录APP
     * @param request
     * @param response
     * @param user
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/login_app")
    public void loginUserAPP(HttpServletRequest request, HttpServletResponse response, TbUser user) throws IOException {
        Integer userId = userService.loginUserByObj(user);
        TbUser updateUser = new TbUser();
        updateUser.setUserId(userId);
        updateUser.setLastLoginTime(new Date());
        updateUser.setLastLoginIp(HttpUtils.getIpAddress(request));
        userService.updateUserByObj(updateUser);
        Map<String,Object> map= new HashMap<>();
        map.put("params",user);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        if (userId == null){
            // 根据手机号和密码找不到相应的用户
            result = new ResultInfo(Status.LOGIN_ERROR);
            result.setData(null);
        } else{
            // 登录成功
            result = new ResultInfo(Status.SUCCESS);
            result.setData(userId);
        }
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 查询单个用户信息
     * @param response
     * @throws IOException
     */
    @ApiOperation("查询单个用户信息的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", defaultValue = "", required = true)
    })
    @RequestMapping(method = RequestMethod.GET, value = "/info")
    public void selectUserById(HttpServletResponse response,
                           @RequestParam(required = true) Integer userId) throws IOException {
        // 根据userId 查询用户的信息
        TbUser user = userService.selectUserById(userId);
        Map<String,Object> map= new HashMap<>();
        map.put("params",userId);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        if(user == null){
            result = new ResultInfo(Status.ILLEGAL_TOKEN);
            result.setData(null);
        }else {
            result = new ResultInfo(Status.SUCCESS);
            result.setData(user);
        }
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 查询单个用户APP端信息
     * @param response
     * @throws IOException
     */
    @ApiOperation("查询单个用户信息APP端的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", defaultValue = "", required = true)
    })
    @RequestMapping(method = RequestMethod.GET, value = "/info_app")
    public void selectUserByIdAPP(HttpServletResponse response,
                               @RequestParam(required = true) Integer userId) throws IOException {
        // 根据userId 查询用户的信息
        TbUser user = userService.selectUserByIdAPP(userId);
        Map<String,Object> map= new HashMap<>();
        map.put("params",userId);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        if(user == null){
            result = new ResultInfo(Status.ILLEGAL_TOKEN);
            result.setData(null);
        }else {
            result = new ResultInfo(Status.SUCCESS);
            result.setData(user);
        }
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 查找用户
     * @param response
     * @throws IOException
     */
    @ApiOperation("查询用户的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vo", value = "分页对象", defaultValue = "null"),
            @ApiImplicitParam(name = "user", value = "查询条件接收对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/select")
    public void selectUser(HttpServletResponse response,
                           MyVo vo, TbUser user) throws IOException {
        /**
         * Page(current,size)
         * current:当前页,long类型
         * size:每页显示的数量,long类型
         * 可参考其构造方法
         */
        IPage<TbUser> userIPage = userService.selectUser(new Page<>(vo.getCurrent(), vo.getSize()), user);
        Map<String,Object> map= new HashMap<>();
        map.put("params",null);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(userIPage);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 查找所有教师
     * @param response
     * @throws IOException
     */
    @ApiOperation("查询所有教师的接口")
    @RequestMapping(method = RequestMethod.GET, value = "/select_all_teacher")
    public void selectTeachersNoPage(HttpServletResponse response) throws IOException {
        List<TbUser> teachers = userService.selectUsersByTokenNoPage("teacher");
        Map<String,Object> map= new HashMap<>();
        map.put("params",null);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(teachers);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 查找所有没有教师的学生的信息
     * @param response
     * @throws IOException
     */
    @ApiOperation("查询所有没有教师的学生的接口")
    @RequestMapping(method = RequestMethod.GET, value = "/select_all_student_no_teacher")
    public void selectAllStudentNoTeacher(HttpServletResponse response) throws IOException {
        List<TbUser> students = userService.selectAllStudentNoTeacher("student");
        Map<String,Object> map= new HashMap<>();
        map.put("params",null);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(students);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 查找某个教师的学生的信息
     * @param response
     * @throws IOException
     */
    @ApiOperation("查询某个教师的学生的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "teacherId", value = "教师id", defaultValue = "", required = true)
    })
    @RequestMapping(method = RequestMethod.GET, value = "/select_all_student_teacher")
    public void selectStudentListByTeacherId(HttpServletResponse response
            , @RequestParam(required = true)Integer teacherId) throws IOException {
        List<TbUser> students = userService.selectStudentListByTeacherId(teacherId);
        Map<String,Object> map= new HashMap<>();
        map.put("params",teacherId);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        if(students != null && !students.isEmpty()){
            result = new ResultInfo(Status.SUCCESS);
            result.setData(students);
        }else{
            result = new ResultInfo(Status.SUCCESS);
            result.setMessage("该教师下仍未有学生");
            result.setData(null);
        }
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 创建用户（新增用户）
     * @param response
     * @param user
     * @throws IOException
     */
    @ApiOperation("新增用户的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "新增的用户对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/insert")
    public void insertUser(HttpServletResponse response, TbUser user) throws IOException {
        // true 代表插入成功， false 代表用户名（手机号）已经注册过
        userService.insertUserByObj(user);

        // 返回的结果集
        ResultInfo result;
        if(user.getUserId() != null){
            result = new ResultInfo(Status.SUCCESS);
            result.setData(user);
        }else {
            result = new ResultInfo(Status.LOGIN_USER_EXIST); // 该用户已存在
            result.setData(null);
        }
        Map<String,Object> map= new HashMap<>();
        map.put("params",user);
        map.put("method",RequestMethod.POST);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 删除用户
     * @param response
     * @param user
     * @throws IOException
     */
    @ApiOperation("删除用户的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "删除的用户对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public void deleteUser(HttpServletResponse response, TbUser user) throws IOException {
        userService.deleteUserByObj(user);

        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);

        Map<String,Object> map= new HashMap<>();
        map.put("params",user);
        map.put("method",RequestMethod.DELETE);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 注销用户
     * @param response
     * @param user
     * @throws IOException
     */
    @ApiOperation("注销用户的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "注销的用户对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/cancel")
    public void cancelUser(HttpServletResponse response, TbUser user) throws IOException {
        userService.cancelUserByObj(user);

        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);

        Map<String,Object> map= new HashMap<>();
        map.put("params",user);
        map.put("method",RequestMethod.PUT);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 激活用户
     * @param response
     * @param user
     * @throws IOException
     */
    @ApiOperation("激活用户的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "激活的用户对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/activate")
    public void activateUser(HttpServletResponse response, TbUser user) throws IOException {
        userService.activateUserByObj(user);

        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);

        Map<String,Object> map= new HashMap<>();
        map.put("params",user);
        map.put("method",RequestMethod.PUT);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 认证用户
     * @param response
     * @param userId
     * @param roleId
     * @throws IOException
     */
    /*@ApiOperation("认证用户的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "需要认证的用户", required = true),
            @ApiImplicitParam(name = "roleId", value = "需要认证的角色", required = true),
            @ApiImplicitParam(name = "userNum", value = "学号/教工号", required = true)
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/identify")
    public void identifyUser(HttpServletResponse response,
                             @RequestParam(required = true)Integer userId,
                             @RequestParam(required = true)Integer roleId,
                             @RequestParam(required = true)String userNum) throws IOException {
        boolean identifyResult = userService.identifyUserRoleByUserIdAndRoleId(userId, roleId, userNum);

        // 返回的结果集
        ResultInfo result;
        if(identifyResult){
            // 认证成功
            result = new ResultInfo(Status.SUCCESS);
            result.setData(null);
        }else{
            // 认证失败
            result = new ResultInfo(Status.WARN);
            result.setData(null);
        }
        Map<String,Object> map= new HashMap<>();
        // 参数
        Map<String, Integer> params = new HashMap<>();
        params.put("userId", userId);
        params.put("roleId", roleId);
        map.put("params",params);
        map.put("method",RequestMethod.PUT);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }*/


    /**
     * 修改用户密码
     * @param response
     * @param userNum
     * @param userPassword
     * @param newPassword
     * @throws IOException
     */
    @ApiOperation("修改用户密码的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userNum", value = "学号/教工号", defaultValue = ""),
            @ApiImplicitParam(name = "userPassword", value = "原密码", defaultValue = ""),
            @ApiImplicitParam(name = "newPassword", value = "新密码", defaultValue = "")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/update_user_pass")
    public void updateUserPass(HttpServletResponse response,
                               String userNum, String userPassword, String newPassword) throws IOException {
        boolean updateResult = userService.updateUserPass(userNum, userPassword, newPassword);

        // 返回的结果集
        ResultInfo result;
        if(updateResult){
            // success
            result = new ResultInfo(Status.SUCCESS);
            result.setData(null);
        }else{
            // 学号已经存在
            result = new ResultInfo(Status.WARN);
            result.setMessage("修改失败，原密码输入不正确");
            result.setData(null);
        }
        Map<String,Object> map= new HashMap<>();
        Map<String,Object> params= new HashMap<>();
        params.put("userNum",userNum);
        params.put("userPassword",userPassword);
        params.put("newPassword",newPassword);

        map.put("params",params);
        map.put("method",RequestMethod.PUT);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }





    /**
     * 修改用户
     * @param response
     * @param user
     * @throws IOException
     */
    @ApiOperation("修改用户的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "修改的用户对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public void updateUser(HttpServletResponse response, TbUser user) throws IOException {
        boolean updateResult = userService.updateUserByObj(user);

        // 返回的结果集
        ResultInfo result;
        if(updateResult){
            // success
            result = new ResultInfo(Status.SUCCESS);
            result.setData(null);
        }else{
            // 学号已经存在
            result = new ResultInfo(Status.LOGIN_USER_EXIST);
            result.setMessage("修改失败，新学号对应的"+Status.LOGIN_USER_EXIST.message);
            result.setData(null);
        }
        Map<String,Object> map= new HashMap<>();
        map.put("params",user);
        map.put("method",RequestMethod.PUT);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 重置密码
     * @param response
     * @param userId
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/reset_pass")
    public void resetUserPass(HttpServletResponse response, Integer userId) throws IOException {
        boolean updateResult = userService.resetUserPass(userId);

        // 返回的结果集
        ResultInfo result;
        if(updateResult){
            // success
            result = new ResultInfo(Status.SUCCESS);
            result.setData(null);
        }else{
            // 学号已经存在
            result = new ResultInfo(Status.WARN);
            result.setMessage("密码重置失败， 发生不知名错误");
            result.setData(null);
        }
        Map<String,Object> map= new HashMap<>();
        map.put("params",userId);
        map.put("method",RequestMethod.PUT);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 教师选取学生
     * @param response
     * @param teacherId
     * @param studentIds
     * @throws IOException
     */
    @ApiOperation("教师选取学生的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "teacherId", value = "需要选择学生的教师", required = true),
            @ApiImplicitParam(name = "studentIds", value = "学生集合")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/choose_students")
    public void grantRole(HttpServletResponse response,
                          @RequestParam(required = true) Integer teacherId,
                          @RequestParam String studentIds ) throws IOException {

        List<Integer> studentIdList = new ArrayList<>();
        // permissionIds不为null且不为空， 重新组装permissionIdList
        if(studentIds != null && !studentIds.isEmpty()){
            String[] studentArr = studentIds.split(",");
            for(String s : studentArr){
                int temp = Integer.parseInt(s);
                studentIdList.add(temp);
            }
        }
        // 选取学生操作
        userService.chooseStudentByTeacherIdAndStudentIds(teacherId, studentIdList);

        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);
        Map<String,Object> map= new HashMap<>();
        // 参数
        Map<String, Object> params = new HashMap<>();
        params.put("teacherId", teacherId);
        params.put("studentIds", studentIds);
        map.put("params",params);
        map.put("method",RequestMethod.POST);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 修改用户密码APP
     * @param response
     * @param params
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/update_pass_app")
    public void updateUserPassAPP(HttpServletResponse response,
                                  @RequestBody JSONObject params) throws IOException {
        TbUser tbUser = new TbUser();
        String userNum = (String) params.get("userNum");
        String userPassword = (String) params.get("userPassword");
        String newPassword = (String) params.get("newPassword");

        tbUser.setUserNum(userNum);
        tbUser.setUserPassword(userPassword);

        boolean updateResult = userService.updateUserPassByObjAndNewPassword(tbUser, newPassword);
        // 返回的结果集
        ResultInfo result;
        if(updateResult){
            // success
            result = new ResultInfo(Status.SUCCESS);
            result.setData(null);
        }else{
            // 学号已经存在
            result = new ResultInfo(Status.WARN);
            result.setMessage("修改失败，原密码输入错误 或 修改密码时，发生不知名错误");
            result.setData(null);
        }
        Map<String,Object> map= new HashMap<>();
        map.put("params",params);
        map.put("method",RequestMethod.POST);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 修改用户密码
     * @param response
     * @param user
     * @param newPassword
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/update_pass")
    public void updateUserPass(HttpServletResponse response,
                                  TbUser user, String newPassword) throws IOException {

        boolean updateResult = userService.updateUserPassByObjAndNewPassword(user, newPassword);
        // 返回的结果集
        ResultInfo result;
        if(updateResult){
            // success
            result = new ResultInfo(Status.SUCCESS);
            result.setData(null);
        }else{
            // 学号已经存在
            result = new ResultInfo(Status.WARN);
            result.setMessage("修改失败，原密码输入错误 或 修改密码时，发生不知名错误");
            result.setData(null);
        }
        Map<String,Object> params= new HashMap<>();
        params.put("user", user);
        params.put("newPassword", newPassword);

        Map<String,Object> map= new HashMap<>();
        map.put("params",params);
        map.put("method",RequestMethod.PUT);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }



}
