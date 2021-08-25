package com.scy.running.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scy.running.model.TbPermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scy.running.model.TbUser;

import java.util.List;

/**
 * <p>
 * InnoDB free: 11264 kB 服务类
 * </p>
 *
 * @author scy
 * @since 2021-07-12
 */
public interface ITbPermissionService extends IService<TbPermission> {

    // 根据条件查询权限信息，自带分页功能
    public IPage<TbPermission> selectPermission(Page<TbPermission> page, TbPermission obj);

    public void insertPermissionByObj(TbPermission obj); // 新增权限

    public void deletePermissionByObj(TbPermission obj); // 删除权限（彻底删除，把所有有关的信息都要删除）

    public void updatePermissionByObj(TbPermission obj); // 修改权限

    public void cancelPermissionByObj(TbPermission obj); // 注销权限（修改权限的def_flag标志为 1）

    public void activatePermissionByObj(TbPermission obj); // 激活权限（修改权限的def_flag标志为 0）

    public TbPermission selectPermissionById(Integer permissionId); // 根据权限id查询权限的信息
}
