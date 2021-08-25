package com.scy.running.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scy.running.model.TbPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.imageio.plugins.common.I18N;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * InnoDB free: 11264 kB Mapper 接口
 * </p>
 *
 * @author scy
 * @since 2021-07-12
 */
@Repository
public interface TbPermissionMapper extends BaseMapper<TbPermission> {


    /**
     * <p>
     * 查询 : 根据state状态查询用户列表，分页显示
     * </p>
     *
     * @param page 分页对象,xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
     * @param obj 查询条件对象
     * @return 分页对象
     */
    public IPage<TbPermission> selectPermission(Page<?> page, TbPermission obj);
    public List<Integer> selectListPermissionIdByParentId(Integer parentPermissionId); // 根据父级权限id查找其子集权限的id集

    public void deleteRolePermissionByPermissionId(Integer permissionId); // 根据权限id删除角色的权限
    public void deleteChildPermissionByParentId(Integer permissionId); // 根据父级权限id 删除子级权限

    public void cancelPermissionById(Integer permissionId); // 根据权限id 注销权限
    public void cancelPermissionByParentId(Integer parentPermissionId); // 根据父级权限的id 把父级权限的id设为NULL

    public void activatePermissionById(Integer permissionId); // 根据权限id 激活权限

    public List<TbPermission> selectPermissionListByRoleId(Integer roleId); // 根据角色id查询角色下所有的角色权限

    public Integer selectCountByPermissionName(String permissionName); // 根据权限名称，查询是否已经存在相同的权限名称，用于权限新增的校验

    public TbPermission selectPermissionById(Integer permissionId); // 根据权限id查询权限的信息
}
