package com.scy.running.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scy.running.model.TbPermission;
import com.scy.running.model.TbRole;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * <p>
 * InnoDB free: 11264 kB 服务类
 * </p>
 *
 * @author scy
 * @since 2021-07-09
 */
public interface ITbRoleService extends IService<TbRole> {

    // 根据条件查询角色信息，自带分页功能
    public IPage<TbRole> selectRole(Page<TbRole> page, TbRole obj);

    public List<TbRole> selectRoleNoPage(); // 查询所有的角色信息，不分页

    public void insertRoleByObj(TbRole obj); // 新增角色

    public void deleteRoleByObj(TbRole role); // 删除角色（彻底删除，把所有有关的信息都要删除）

    public void updateRoleByObj(TbRole role); // 修改角色

    public void cancelRoleByObj(TbRole obj); // 注销角色（修改角色的def_flag标志为 1）

    public void activateRoleByObj(TbRole obj); // 激活角色（修改角色的def_flag标志为 0）

    public void grantRolePermissionByRoleIdAndPermissionIds(Integer roleId, List<Integer> permissionIds); // 角色授权操作

    public TbRole selectRoleById(Integer roleId); // 根据角色id查询角色的信息

    public List<Integer> selectRolePermissionByRoleId(Integer roleId); // 根据角色id查询角色权限表中的角色权限id集合
}
