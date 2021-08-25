package com.scy.running.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scy.running.mapper.TbPermissionMapper;
import com.scy.running.mapper.TbPunchClockMapper;
import com.scy.running.model.TbPermission;
import com.scy.running.model.TbUser;
import com.scy.running.mapper.TbUserMapper;
import com.scy.running.service.ITbUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * <p>
 * InnoDB free: 11264 kB; (`role_id`) REFER `scy_running_app/tb_role`(`role_id`) 服务实现类
 * </p>
 *
 * @author scy
 * @since 2021-07-09
 */
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements ITbUserService {

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private TbPermissionMapper permissionMapper;

    @Autowired
    private TbPunchClockMapper punchClockMapper;

    // 获取application.yml中设置的用户重置默认密码
    @Value("${userResetPass}")
    private String userResetPass;

    // 查询用户
    @Override
    public IPage<TbUser> selectUser(Page<TbUser> page, TbUser obj) {
        return userMapper.selectUser(page, obj);
    }

    @Override
    public List<TbUser> selectUsersByTokenNoPage(String token) {
        return userMapper.selectUsersByTokenNoPage(token);
    }

    // 用户登录：根据电话和密码验证(def_flag = 1 --- 已注销的用户不考虑) userId不为null说明用户存在——>登录成功了
    @Override
    public Integer loginUserByObj(TbUser user) {
//        TbUser us =  userMapper.loginUserByObj(user);
        /*TbUser us = null;
        if (userId != null){
            // userId 不为 null， 则说明存在该用户，即登录成功，随后要通过该userId查询到该user的相关信息
            us = userMapper.selectUserById(userId);
            if(us.getRole() != null){
                if(us.getRole().getRoleId() != null){
                    List<TbPermission> permissionList = permissionMapper.selectPermissionListByRoleId(us.getRole().getRoleId());
                    us.getRole().setPermissionList(permissionList);
                }
            }
        }*/
        return userMapper.loginUserByObj(user);
    }

    // 要获取tokenName
    @Override
    public TbUser loginUserAndRoleTokenByObj(TbUser user) {
        return userMapper.loginUserAndRoleTokenByObj(user);
    }

    // 根据用户的id查询到user的所有信息 (包括role， permissionList)
    @Override
    public TbUser selectUserById(Integer userId) {
        TbUser us = null;
        if (userId != null){
            // userId 不为 null， 则说明存在该用户，即登录成功，随后要通过该userId查询到该user的相关信息
            us = userMapper.selectUserById(userId);
            if(us.getRole() != null){
                if(us.getRole().getRoleId() != null){
                    List<TbPermission> permissionList = permissionMapper.selectPermissionListByRoleId(us.getRole().getRoleId());
                    us.getRole().setPermissionList(permissionList);
                }
            }
        }
        return us;
    }

    // 根据用户的id查询到user的所有基本信息APP
    @Override
    public TbUser selectUserByIdAPP(Integer userId) {
        TbUser us = null;
        if (userId != null){
            // userId 不为 null， 则说明存在该用户，即登录成功，随后要通过该userId查询到该user的基本信息
            us = userMapper.selectUserByIdAPP(userId);
        }
        return us;
    }

    /**
     * 新增用户： 首先检查账户是否已经存在
     *           i: 若不存在，则新增
     *           ii: 若存在，则返回提示信息
     * @param obj
     */
    @Override
    public void insertUserByObj(TbUser obj) {
        // boolean result = true;
        // ①首先检查账户是否已经存在
        Map<String, Object> map = new HashMap<>();
        map.put("userNum", obj.getUserNum());
        map.put("userId", null);
        Integer count = userMapper.selectCountByUserNum(map);
        if (count.intValue() == 0){
            // ②i:若不存在，则新增
            userMapper.insert(obj);
        }
            // ②ii: 若存在，则返回提示信息
            //result = false;

        //return result;
    }

    /**
     * 删除用户: 删除有关的打卡跑步记录
     *          删除用户
     * @param obj
     */
    @Override
    public void deleteUserByObj(TbUser obj) {
        // ①删除有关的打卡跑步记录
        punchClockMapper.deletePunchClockByUserId(obj.getUserId());
        // ②判断是否是教师, 通过roleToken判断，如果是，则把对应的学生的teacher_id设为null
        if(obj.getRoleToken().equals("teacher")){
            userMapper.updateStudentTeacherIdToNullByUserId(obj.getUserId());
        }
        // ③删除用户
        userMapper.deleteById(obj.getUserId());
    }

    // 修改用户信息
    @Override
    public boolean updateUserByObj(TbUser obj) {
        boolean result = false;
        // ①首先检查账户是否已经存在
        Map<String, Object> map = new HashMap<>();
        map.put("userNum", obj.getUserNum());
        map.put("userId", obj.getUserId());
        Integer count = userMapper.selectCountByUserNum(map);
        // 说明查询出来的是
        if (count.intValue() == 0){
            // ②i:若不存在，根据id修改对象
            userMapper.updateById(obj);
            result = true;
        }
        return result;
    }

    /**
     * 注销用户： 用户的def_flag设为 1
     * @param obj
     */
    @Override
    public void cancelUserByObj(TbUser obj) {
        // 用户的def_flag设为 1
        userMapper.cancelUserById(obj.getUserId());
    }

    /**
     * 激活用户： 用户的def_flag设为 0
     * @param obj
     */
    @Override
    public void activateUserByObj(TbUser obj) {
        // 用户的def_flag设为 0
        userMapper.activateUserById(obj.getUserId());
    }


    // 查找所有没有教师的学生的信息
    @Override
    public List<TbUser> selectAllStudentNoTeacher(String token) {
        return userMapper.selectAllStudentNoTeacherByToken(token);
    }

    // 根据teacherId查询该老师下的所有学生
    @Override
    public List<TbUser> selectStudentListByTeacherId(Integer teacherId) {
        return userMapper.selectStudentListByTeacherId(teacherId);
    }

    // 选取学生操作
    @Override
    public void chooseStudentByTeacherIdAndStudentIds(Integer teacherId, List<Integer> studentIds) {
        // ①首先根据teacherId查询到原本有多少的学生
        List<Integer> originStudentIds = userMapper.selectStudentIdsByTeacherId(teacherId);
        /**
         * ②然后和传来的studentIds做对比，筛选出相同的，不同的
         */
        // 创建原本有的，现在没有了的集合
        List<Integer> originHaveIds = new ArrayList<>();
        // 创建原本没有的，现在有了的集合
//        List<Integer> nowHaveIds = new ArrayList<>();
        // 对比原来的ids和传入进来的ids，找出不同的ids
        for(int i=0; i<originStudentIds.size(); i++){
            // 记录第 i 个原来的学生id
            int tempI = ((Integer)originStudentIds.get(i)).intValue();
            // 设定一个标志，是否移除了
            boolean isRemove = false;
            // 遍历传入的ids，进行比较
            for(int j=0; j<studentIds.size(); j++){
                // 如果说，传入的ids有相同的，则代表是不用处理的，应该移除
                if(tempI == ((Integer)studentIds.get(j)).intValue()){
                    // 修改移除标志，表明已经移除了
                    isRemove = true;
                    studentIds.remove(j);
                    // 移除后要跳出该层循环，直接比较下一个
                    break;
                }
            }
            // 如果没被移除，则该元素加入到originHaveIds集合中去
            if(!isRemove){
                originHaveIds.add(tempI);
            }
        }
        // 处理后的studentIds集合就是原本没有的，现在有了的集合

        /**
         * ③对不用的studentId做操作：
         *          i: 原本有的，现在没有了， 要进行修改
         *          ii：原本没有的，现在有了，也要进行修改
         */
        // ③ i: 原本有的，现在没有了， 要进行修改
        for(Integer id : originHaveIds){
            userMapper.updateTeacherToNextByUserId(null, id);
        }
        // ③ ii：原本没有的，现在有了，也要进行修改
        for(Integer id : studentIds){
            userMapper.updateTeacherToNextByUserId(teacherId, id);
        }

    }

    /**
     * 修改密码： ①先根据userNum， userPassword，查询用户是否存在
     *          ②再根据newPassword和userId[从第一步获取] 修改密码
     * @param userNum
     * @param userPassword
     * @param newPassword
     * @return
     */
    @Override
    public boolean updateUserPass(String userNum, String userPassword, String newPassword) {
        boolean result = false;
        // 处理参数
        TbUser user = new TbUser();
        user.setUserNum(userNum);
        user.setUserPassword(userPassword);
        // 根据userNum， userPassword，查询用户是否存在
        Integer userId = userMapper.loginUserByObj(user);
        if (userId != null){
            TbUser updateUser = new TbUser();
            updateUser.setUserId(userId);
            updateUser.setUserPassword(newPassword);
            updateUser.setUpdateUser(userId);
            updateUser.setUpdateTime(new Date());
            // 修改密码
            userMapper.updateById(updateUser);
            result = true;
        }
        return result;
    }

    /**
     * 修改用户得密码， 首先根据学号/教工号 和 原密码 查找用户，若存在，则修改， 若不存在，则返回原密码错误信息
     * @param obj
     * @param newPassword
     * @return
     */
    @Override
    public boolean updateUserPassByObjAndNewPassword(TbUser obj, String newPassword) {
        boolean result = false;
        Integer userId = userMapper.selectUserIdByUserNumAndPassword(obj);
        if(userId != null){
            TbUser tbUser = new TbUser();
            tbUser.setUserId(userId);
            tbUser.setUserPassword(newPassword);

            userMapper.updateById(tbUser);
            result = true;
        }
        return result;
    }

    // 重置用户密码
    @Override
    public boolean resetUserPass(Integer userId) {
        boolean result = false;
        TbUser user = new TbUser();
        user.setUserId(userId);
        user.setUserPassword(userResetPass);
        userMapper.updateById(user);
        result = true;
        return result;
    }

    /**
     * 认证用户的角色: 首先查看用户是否已经加入待认证列表（即 分配了学号或教工号）
     *              若加入，则认证成功
     *              若未加入，则认证失败
     * @param userId
     * @param roleId
     * @return
     */
    /*@Override
    public boolean identifyUserRoleByUserIdAndRoleId(Integer userId, Integer roleId, String userNum) {
        boolean result = false;
        // ① 首先查看用户是否已经加入待认证列表（即 分配了学号或教工号）
        String userNum = userMapper.selectUserNumByUserId(userId);
        if(userNum != null && !userNum.equals("")){
            // 认证sql所需要的参数
            Map<String, Integer> map = new HashMap<>();
            map.put("userId", userId);
            map.put("roleId", roleId);
            // 若加入，则认证成功
            userMapper.identifyUserRoleByUserIdAndRoleId(map);
            // 表明认证成功
            result = true;
        }
        return result;
    }*/


}
