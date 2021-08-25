package com.scy.running.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scy.running.model.TbClass;
import com.scy.running.model.TbCollege;
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
public interface TbClassMapper extends BaseMapper<TbClass> {

    public IPage<TbClass> selectClass(Page<?> page, TbClass obj);

    public Integer selectCountByClassCode(Map<String, Object> map); // 根据班级代码查询是否存在学院

    public TbClass selectClassById(Integer classId); // 根据班级id查询班级的信息

    public void updateUserClassIdToNullByClassId(Integer classId); // 把班级下的学生的classId设为null


    public List<TbClass> selectClassAll(); // 查找所有班级

    public List<Integer> selectUserIdByClassIdList(List<Integer> classIdList); // 根据classIdList查询到相应的userId集合

}
