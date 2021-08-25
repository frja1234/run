package com.scy.running.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scy.running.mapper.TbClassMapper;
import com.scy.running.mapper.TbCollegeMapper;
import com.scy.running.model.TbClass;
import com.scy.running.model.TbCollege;
import com.scy.running.service.ITbClassService;
import com.scy.running.service.ITbCollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * InnoDB free: 11264 kB 服务实现类
 * </p>
 *
 * @author scy
 * @since 2021-08-12
 */
@Service
public class TbClassServiceImpl extends ServiceImpl<TbClassMapper, TbClass> implements ITbClassService {

    @Autowired
    private TbClassMapper classMapper;

    /**
     * 分页查询
     * @param page
     * @param obj
     * @return
     */
    @Override
    public IPage<TbClass> selectClass(Page<TbClass> page, TbClass obj) {
        return classMapper.selectClass(page, obj);
    }

    /**
     * 新增班级，先根据classCode 查询是否已经存在班级
     *         不存在则新增
     * @param obj
     */
    @Override
    public void insertClassByObj(TbClass obj) {
        Map<String, Object> map = new HashMap<>();
        map.put("classCode", obj.getClassCode());
        map.put("classId", null);
        Integer count = classMapper.selectCountByClassCode(map);
        if (count.intValue() == 0){
            // ②i:若不存在，则新增
            classMapper.insert(obj);
        }
    }

    /**
     * 根据班级id查询班级的信息
     * @param classId
     * @return
     */
    @Override
    public TbClass selectClassById(Integer classId) {
        return classMapper.selectClassById(classId);
    }

    /**
     * 修改班级
     * @param obj
     * @return
     */
    @Override
    public boolean updateClassByObj(TbClass obj) {
        boolean result = false;
        // ①首先检查班级是否已经存在
        Map<String, Object> map = new HashMap<>();
        map.put("classCode", obj.getClassCode());
        map.put("classId", obj.getClassId());
        Integer count = classMapper.selectCountByClassCode(map);
        // 说明查询出来的是
        if (count.intValue() == 0){
            // ②i:若不存在，根据id修改对象
            classMapper.updateById(obj);
            result = true;
        }
        return result;
    }

    /**
     * 删除班级， 要把对应用户的班级id设为null
     * @param obj
     */
    @Override
    public void deleteClassByObj(TbClass obj) {
        Integer classId = obj.getClassId();
        // ① 把班级下的学生的classId设为null
        classMapper.updateUserClassIdToNullByClassId(classId);
        // ② 删除班级
        classMapper.deleteById(classId);
    }

    @Override
    public List<TbClass> selectClassAll() {
        return classMapper.selectClassAll();
    }
}
