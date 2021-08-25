package com.scy.running.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scy.running.model.TbCollege;
import com.scy.running.model.TbPermission;
import com.scy.running.model.TbPunchClock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * InnoDB free: 11264 kB Mapper 接口
 * </p>
 *
 * @author scy
 * @since 2021-08-12
 */
@Repository
public interface TbCollegeMapper extends BaseMapper<TbCollege> {

    public IPage<TbCollege> selectCollege(Page<?> page, TbCollege obj);

    public List<TbCollege> selectCollegeAll(); // 查找所有学院
    public Integer selectCountByCollegeCode(Map<String, Object> map); // 根据学院代码查询是否存在学院

    public TbCollege selectCollegeById(Integer collegeId); // 根据学院id查询学院的信息

    public void updateClassCollegeIdToNullByCollegeId(Integer collegeId); // 把学院下的班级的collegeId设为null


}

