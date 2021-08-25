package com.scy.running.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scy.running.model.TbClass;
import com.scy.running.model.TbCollege;

import java.util.List;

/**
 * <p>
 * InnoDB free: 11264 kB 服务类
 * </p>
 *
 * @author scy
 * @since 2021-08-12
 */
public interface ITbClassService extends IService<TbClass> {

    public IPage<TbClass> selectClass(Page<TbClass> page, TbClass obj);

    public void insertClassByObj(TbClass obj); // 新增班级

    public TbClass selectClassById(Integer classId); // 根据班级id查询班级的信息

    public boolean updateClassByObj(TbClass obj); // 修改班级

    public void deleteClassByObj(TbClass obj); // 删除学院

    public List<TbClass> selectClassAll(); // 查找所有班级

}
