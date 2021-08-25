package com.scy.running.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scy.running.model.TbPermission;
import com.scy.running.mapper.TbPermissionMapper;
import com.scy.running.service.ITbPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * InnoDB free: 11264 kB 服务实现类
 * </p>
 *
 * @author scy
 * @since 2021-07-12
 */
@Service
public class TbPermissionServiceImpl extends ServiceImpl<TbPermissionMapper, TbPermission> implements ITbPermissionService {

    @Autowired
    private TbPermissionMapper permissionMapper;

    /**
     * 查找权限信息
     * @param page
     * @param obj
     * @return
     */
    @Override
    public IPage<TbPermission> selectPermission(Page<TbPermission> page, TbPermission obj) {
        // 不进行 count sql 优化，解决 MP 无法自动优化 SQL 问题，这时候你需要自己查询 count 部分
        // page.setOptimizeCountSql(false);
        // 当 total 为小于 0 或者设置 setSearchCount(false) 分页插件不会进行 count 查询
        // 要点!! 分页返回的对象与传入的对象是同一个
        return permissionMapper.selectPermission(page, obj);
    }

    // 新增权限
    @Override
    public void insertPermissionByObj(TbPermission obj) {
        // 插入之前先检查该权限名称，是否已经存在
        Integer count = permissionMapper.selectCountByPermissionName(obj.getPermissionName());
        if(count != null && count.intValue() == 0){
            // 插入对象
            permissionMapper.insert(obj);
        }
    }

    // 递归删除权限： 子集权限
    private void recursionDeletePermission(Integer permissionId){
        // ①查找权限子集的id集合
        List<Integer> childPermissionIdList = permissionMapper.selectListPermissionIdByParentId(permissionId);
        // 判断：子集id不为空
        if(childPermissionIdList != null && !childPermissionIdList.isEmpty()){
            // ②删除其权限子集下的角色权限
            for (Integer childId : childPermissionIdList){
                // 递归遍历
                recursionDeletePermission(childId);
                // permissionMapper.deleteRolePermissionByPermissionId(childId);
            }
            // ③删除权限子集(根据父级权限的id，直接删除所有子集权限)
            // permissionMapper.deleteChildPermissionByParentId(permissionId);
        }
        // ④删除自己的角色权限
        permissionMapper.deleteRolePermissionByPermissionId(permissionId);
        // ⑤删除自己权限
        permissionMapper.deleteById(permissionId);
    }

    // 删除权限，先删除其权限子集下的角色权限，再删除其权限子集，然后删除自己的角色权限，最后删除自己权限
    @Override
    public void deletePermissionByObj(TbPermission obj) {
        // 递归删除
        recursionDeletePermission(obj.getPermissionId());
        // ①查找权限子集的id集合
//        List<Integer> childPermissionIdList = permissionMapper.selectListPermissionIdByParentId(obj.getPermissionId());
//        // 判断：子集id不为空
//        if(childPermissionIdList != null && !childPermissionIdList.isEmpty()){
//            // ②删除其权限子集下的角色权限
//            for (Integer childId : childPermissionIdList){
//                permissionMapper.deleteRolePermissionByPermissionId(childId);
//            }
//            // ③删除权限子集(根据父级权限的id，直接删除所有子集权限)
//            permissionMapper.deleteChildPermissionByParentId(obj.getPermissionId());
//        }
//        // ④删除自己的角色权限
//        permissionMapper.deleteRolePermissionByPermissionId(obj.getPermissionId());
//        // ⑤删除自己权限
//        permissionMapper.deleteById(obj.getPermissionId());
    }

    // 修改权限
    @Override
    public void updatePermissionByObj(TbPermission obj) {
        // 根据id修改对象
        permissionMapper.updateById(obj);
    }

    /**
     * 注销权限：先把自身的def_flag设为1，表示该权限已被注销
     *          然后把 作为父级权限的子集权限的 parent_permission_id 设为null
     * @param obj
     */
    @Override
    public void cancelPermissionByObj(TbPermission obj) {
        // ①先把自身的def_flag设为1，表示该权限已被注销
        permissionMapper.cancelPermissionById(obj.getPermissionId());

        // ②作为父级权限的子集权限的 parent_permission_id 设为null
        permissionMapper.cancelPermissionByParentId(obj.getPermissionId());

        // ③把角色权限表(tb_role_permission)中的相关信息删掉
        //permissionMapper.deleteRolePermissionByPermissionId(obj.getPermissionId());
    }

    /**
     * 激活权限: 把自身的def_flag设为 0，表示该权限已被激活
     * @param obj
     */
    @Override
    public void activatePermissionByObj(TbPermission obj) {
        // 把自身的def_flag设为 0，表示该权限已被激活
        permissionMapper.activatePermissionById(obj.getPermissionId());
    }

    /**
     * 根据权限id查询权限的信息
     * @param permissionId
     * @return
     */
    @Override
    public TbPermission selectPermissionById(Integer permissionId) {
        return permissionMapper.selectPermissionById(permissionId);
    }
}
