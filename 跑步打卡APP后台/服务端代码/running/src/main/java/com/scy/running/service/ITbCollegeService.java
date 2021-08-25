package com.scy.running.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scy.running.model.*;

import java.util.List;

/**
 * <p>
 * InnoDB free: 11264 kB 服务类
 * </p>
 *
 * @author scy
 * @since 2021-08-12
 */
public interface ITbCollegeService extends IService<TbCollege> {

    public IPage<TbCollege> selectCollege(Page<TbCollege> page, TbCollege obj);

    public List<TbCollege> selectCollegeAll(); // 查找所有学院

    public void insertCollegeByObj(TbCollege obj); // 新增学院

    public TbCollege selectCollegeById(Integer collegeId); // 根据学院id查询学院的信息

    public boolean updateCollegeByObj(TbCollege obj); // 修改学院

    public void deleteCollegeByObj(TbCollege obj); // 删除学院


}
