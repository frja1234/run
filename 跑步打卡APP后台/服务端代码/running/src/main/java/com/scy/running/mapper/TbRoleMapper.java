package com.scy.running.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scy.running.model.TbPermission;
import com.scy.running.model.TbRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * InnoDB free: 11264 kB Mapper 接口
 * </p>
 *
 * @author scy
 * @since 2021-07-09
 */
@Repository
public interface TbRoleMapper extends BaseMapper<TbRole> {


    /**
     * <p>
     * 查询 : 根据state状态查询用户列表，分页显示
     * </p>
     *
     * @param page 分页对象,xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
     * @param obj 查询条件对象
     * @return 分页对象
     */
    public IPage<TbRole> selectRole(Page<?> page, TbRole obj); // 查找角色

    public List<TbRole> selectRoleNoPage(); // 查询所有的角色信息，不分页

    public void deleteRolePermissionByRoleId(Integer roleId); // 根据角色id删除角色权限

    public void cancelRoleById(Integer roleId); // 根据id 注销角色
    public void cancelUserRoleByRoleId(Integer roleId); // 根据角色id 把用户表中对应呃的角色id 设为 NULL

    public void activateRoleById(Integer roleId); // 根据id 激活角色

    public void grantRolePermissionByRoleIdAndPermissionId(Integer roleId, Integer permissionId); // 角色授权操作

    public Integer selectCountByRoleCode(String roleCode); // 根据角色代码，查询是否已经存在相同的角色代码，用于角色新增的校验

    public TbRole selectRoleById(Integer roleId); // 根据角色id查询角色的信息

    public List<Integer> selectRolePermissionByRoleId(Integer roleId); // 根据角色id查询角色权限表中的角色权限id集合
}
