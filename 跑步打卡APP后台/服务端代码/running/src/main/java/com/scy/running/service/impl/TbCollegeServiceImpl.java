package com.scy.running.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scy.running.mapper.TbCollegeMapper;
import com.scy.running.mapper.TbPunchClockMapper;
import com.scy.running.model.TbCollege;
import com.scy.running.model.TbPunchClock;
import com.scy.running.service.ITbCollegeService;
import com.scy.running.service.ITbPunchClockService;
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
public class TbCollegeServiceImpl extends ServiceImpl<TbCollegeMapper, TbCollege> implements ITbCollegeService {

    @Autowired
    private TbCollegeMapper collegeMapper;


    /**
     * 分页按条件查询学院信息
     * @param page
     * @param obj
     * @return
     */
    @Override
    public IPage<TbCollege> selectCollege(Page<TbCollege> page, TbCollege obj) {
        return collegeMapper.selectCollege(page, obj);
    }

    // 查找所有学院
    @Override
    public List<TbCollege> selectCollegeAll() {
        return collegeMapper.selectCollegeAll();
    }

    /**
     * 首先要检查 学院的代码是否已经存在了，学院代码是唯一的， 如果不存在，就直接插入
     * @param obj
     */
    @Override
    public void insertCollegeByObj(TbCollege obj) {
        // ①首先学院的代码是否已经存在了
        Map<String, Object> map = new HashMap<>();
        map.put("collegeCode", obj.getCollegeCode());
        map.put("collegeId", null);
        Integer count = collegeMapper.selectCountByCollegeCode(map);
        if (count.intValue() == 0){
            // ②i:若不存在，则新增
            collegeMapper.insert(obj);
        }
    }

    /**
     * 根据学院的id查询学院的信息
     * @param collegeId
     * @return
     */
    @Override
    public TbCollege selectCollegeById(Integer collegeId) {
        return collegeMapper.selectCollegeById(collegeId);
    }


    /**
     * 修改学院
     * @param obj
     * @return
     */
    @Override
    public boolean updateCollegeByObj(TbCollege obj) {
        boolean result = false;
        // ①首先检查学院是否已经存在
        Map<String, Object> map = new HashMap<>();
        map.put("collegeCode", obj.getCollegeCode());
        map.put("collegeId", obj.getCollegeId());
        Integer count = collegeMapper.selectCountByCollegeCode(map);
        // 说明查询出来的是
        if (count.intValue() == 0){
            // ②i:若不存在，根据id修改对象
            collegeMapper.updateById(obj);
            result = true;
        }
        return result;
    }


    /**
     * 删除学院， 首先需要把学院下的班级的collegeId设为null
     *          然后删除学院
     * @param obj
     */
    @Override
    public void deleteCollegeByObj(TbCollege obj) {
        Integer collegeId = obj.getCollegeId();
        // ① 把学院下的班级的collegeId设为null
        collegeMapper.updateClassCollegeIdToNullByCollegeId(collegeId);
        // ② 删除学院
        collegeMapper.deleteById(collegeId);
    }
}
