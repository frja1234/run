package com.scy.running.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scy.running.model.TbPermission;
import com.scy.running.model.TbRole;
import com.scy.running.model.TbUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * InnoDB free: 11264 kB; (`role_id`) REFER `scy_running_app/tb_role`(`role_id`) 服务类
 * </p>
 *
 * @author scy
 * @since 2021-07-09
 */
public interface ITbUserService extends IService<TbUser> {

    // 根据条件查询用户信息，自带分页功能
    public IPage<TbUser> selectUser(Page<TbUser> page, TbUser obj);

    public List<TbUser> selectUsersByTokenNoPage(String token); // 查询所有的教师信息，不分页

    public Integer loginUserByObj(TbUser user); // 用户登录查询 返回用户的userId
    public TbUser loginUserAndRoleTokenByObj(TbUser user); // 用户登录查询 返回用户的userId

    public TbUser selectUserById(Integer userId); // 根据用户的id查询到user的所有信息
    public TbUser selectUserByIdAPP(Integer userId); // 根据用户的id查询到user的所有信息

    public void insertUserByObj(TbUser obj); // 新增(创建)用户

    public void deleteUserByObj(TbUser obj); // 删除用户（彻底删除，把所有有关的信息都要删除）

    public boolean updateUserByObj(TbUser obj); // 修改用户

    public void cancelUserByObj(TbUser obj); // 注销用户（修改用户的def_flag标志为 1）

    public void activateUserByObj(TbUser obj); // 激活用户（修改用户的def_flag标志为 0）

//    public boolean identifyUserRoleByUserIdAndRoleId(Integer userId, Integer roleId, String userNum); // 认证用户的角色

    public List<TbUser> selectAllStudentNoTeacher(String token); // 查找所有没有教师的学生的信息

    public List<TbUser> selectStudentListByTeacherId(Integer teacherId); // 根据teacherId查询该老师下的所有学生

    public void chooseStudentByTeacherIdAndStudentIds(Integer teacherId, List<Integer> studentIds); // 选取学生操作

    public boolean updateUserPass(String userNum, String userPassword, String newPassword); // 修改用户密码

    public boolean updateUserPassByObjAndNewPassword(TbUser obj, String newPassword); // 修改用户密码

    public boolean resetUserPass(Integer userId); // 重置用户密码
}
