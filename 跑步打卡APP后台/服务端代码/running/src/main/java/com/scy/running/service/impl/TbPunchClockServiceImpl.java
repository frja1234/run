package com.scy.running.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scy.running.mapper.TbPunchClockMapper;
import com.scy.running.mapper.TbTaskMapper;
import com.scy.running.model.TbPunchClock;
import com.scy.running.model.TbTask;
import com.scy.running.service.ITbPunchClockService;
import com.scy.running.service.ITbTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * InnoDB free: 11264 kB 服务实现类
 * </p>
 *
 * @author scy
 * @since 2021-08-12
 */
@Service
public class TbPunchClockServiceImpl extends ServiceImpl<TbPunchClockMapper, TbPunchClock> implements ITbPunchClockService {

    @Autowired
    private TbPunchClockMapper punchClockMapper;

    // 根据userId查询到学生当天的任务信息
    @Override
    public TbPunchClock selectPunchClockByUserIdApp(Integer userId) {
        List<TbPunchClock> tbPunchClocks = punchClockMapper.selectPunchClockByUserIdApp(userId);
        if(tbPunchClocks != null && tbPunchClocks.size() > 0){
            return tbPunchClocks.get(0);
        }else{
            return null;
        }
    }

    // 修改打卡记录
    @Override
    public void updatePunchClockByObj(TbPunchClock obj) {
        // 根据id修改对象
        punchClockMapper.updateById(obj);
    }
}
