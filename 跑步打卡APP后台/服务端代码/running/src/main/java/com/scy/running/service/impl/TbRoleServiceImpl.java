package com.scy.running.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scy.running.mapper.TbUserMapper;
import com.scy.running.model.TbPermission;
import com.scy.running.model.TbRole;
import com.scy.running.mapper.TbRoleMapper;
import com.scy.running.model.TbUser;
import com.scy.running.service.ITbRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * InnoDB free: 11264 kB 服务实现类
 * </p>
 *
 * @author scy
 * @since 2021-07-09
 */
@Service
public class TbRoleServiceImpl extends ServiceImpl<TbRoleMapper, TbRole> implements ITbRoleService {

    @Autowired
    private TbRoleMapper roleMapper;

    @Autowired
    private TbUserMapper userMapper;

    // 查找角色信息
    @Override
    public IPage<TbRole> selectRole(Page<TbRole> page, TbRole obj) {
        return roleMapper.selectRole(page, obj);
    }

    // 查询所有的角色信息，不分页
    @Override
    public List<TbRole> selectRoleNoPage() {
        return roleMapper.selectRoleNoPage();
    }

    // 新增角色
    @Override
    public void insertRoleByObj(TbRole obj) {
        // 插入之前先检查该角色代码，是否已经存在
        Integer count = roleMapper.selectCountByRoleCode(obj.getRoleCode());
        if(count != null && count.intValue() == 0){
            // 新增一个角色对象
            roleMapper.insert(obj);
        }
    }

    // 删除角色：首先删除角色对应的角色权限，再把用户表中的角色设为空，最后删除角色
    @Override
    public void deleteRoleByObj(TbRole role) {
        // ①删除角色对应的角色权限
        roleMapper.deleteRolePermissionByRoleId(role.getRoleId());

        // ②用户表中的角色设为空
        roleMapper.cancelUserRoleByRoleId(role.getRoleId());

        // ③删除角色
        roleMapper.deleteById(role.getRoleId());
    }


    // 修改角色
    @Override
    public void updateRoleByObj(TbRole role) {
        // 根据角色id修改角色一般信息
        roleMapper.updateById(role);
    }

    /**
     * 注销角色: 首先修改角色的def_flag标志为 1
     *          然后再把对应的用户表中的角色id 变为 null
     * @param obj
     */
    @Override
    public void cancelRoleByObj(TbRole obj) {
        // ①首先修改角色的def_flag标志为 1
        roleMapper.cancelRoleById(obj.getRoleId());
        // ②然后再把对应的用户表中的角色id 变为 null
        roleMapper.cancelUserRoleByRoleId(obj.getRoleId());
    }

    /**
     * 激活角色: 修改角色的def_flag标志为 0
     * @param obj
     */
    @Override
    public void activateRoleByObj(TbRole obj) {
        // 修改角色的def_flag标志为 0
        roleMapper.activateRoleById(obj.getRoleId());
    }

    /**
     * 授权操作: 删掉角色权限表(tb_role_permission) 中对应角色id的所有权限
     *          根据权限id集合新增新的角色权限
     * @param roleId
     * @param permissionIds 权限id集合
     */
    @Override
    public void grantRolePermissionByRoleIdAndPermissionIds(Integer roleId, List<Integer> permissionIds) {
        // ① 删掉角色权限表(tb_role_permission) 中对应角色id的所有权限
        roleMapper.deleteRolePermissionByRoleId(roleId);
        // ② 根据权限id集合新增新的角色权限
        for(Integer permissionId : permissionIds){
            roleMapper.grantRolePermissionByRoleIdAndPermissionId(roleId, permissionId);
        }
    }

    // 根据角色id查询角色的信息
    @Override
    public TbRole selectRoleById(Integer roleId) {
        return roleMapper.selectRoleById(roleId);
    }

    // 根据角色id查询角色权限表中的角色权限id集合
    @Override
    public List<Integer> selectRolePermissionByRoleId(Integer roleId) {
        return roleMapper.selectRolePermissionByRoleId(roleId);
    }


}
