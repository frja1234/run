package com.scy.running.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.scy.running.model.TbPermission;
import com.scy.running.model.TbUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * InnoDB free: 11264 kB; (`role_id`) REFER `scy_running_app/tb_role`(`role_id`) Mapper 接口
 * </p>
 *
 * @author scy
 * @since 2021-07-09
 */
@Repository
public interface TbUserMapper extends BaseMapper<TbUser> {

    /**
     * <p>
     * 查询 : 根据state状态查询用户列表，分页显示
     * </p>
     *
     * @param page 分页对象,xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
     * @param obj 查询条件对象
     * @return 分页对象
     */
    public IPage<TbUser> selectUser(Page<?> page, TbUser obj);

    public TbUser selectUserById(Integer userId);
    public TbUser selectUserByIdAPP(Integer userId);

    public Integer loginUserByObj(TbUser user); // 用户登录 根据userNum和密码验证(def_flag = 1 --- 已注销的用户不考虑)
    public TbUser loginUserAndRoleTokenByObj(TbUser user); // 用户登录查询 返回用户的userId


    public Integer selectUserIdByUserNumAndPassword(TbUser user);
//    public Integer selectCountByUserCellPhone(String userCellPhone); // 根据手机号查询是否已经存在用户

    public Integer selectCountByUserNum(Map<String, Object> map); // 根据学号/教工号查询是否已经存在用户
//    public TbUser selectUserByUserNum(String userNum); // 根据学号/教工号查询是否已经存在用户
    public String selectUserNumByUserId(Integer userId); // 根据用户的id查询是否分配了学号

    public List<TbUser> selectUsersByTokenNoPage(String token); // 查询所有的教师信息，不分页

    public void cancelUserById(Integer Integer); // 注销用户（修改用户的def_flag标志为 1）

    public void activateUserById(Integer Integer); // 激活用户（修改用户的def_flag标志为 0）

    public void updateStudentTeacherIdToNullByUserId(Integer userId); // 根据userId把对应的学生的teacher_id设为null

//    public void identifyUserRoleByUserIdAndRoleId(Map<String, Integer> map); // 认证用户的角色

    public List<TbUser> selectAllStudentNoTeacherByToken(String token); // 查找所有没有教师的学生的信息

    public List<TbUser> selectStudentListByTeacherId(Integer teacherId); // 根据teacherId查询该老师下的所有学生

    public List<Integer> selectStudentIdsByTeacherId(Integer teacherId); // 根据teacherId查询该老师下的所有学生id

    public void updateTeacherToNextByUserId(Integer next, Integer userId); // 通过学生id修改学生的教师id，修改成next
}
