package com.scy.running.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scy.running.model.TbPunchClock;
import com.scy.running.model.TbTask;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * InnoDB free: 11264 kB Mapper 接口
 * </p>
 *
 * @author scy
 * @since 2021-08-12
 */
@Repository
public interface TbPunchClockMapper extends BaseMapper<TbPunchClock> {

    public List<TbPunchClock> selectPunchClockByUserIdApp(Integer userId); // 根据userId查找当天的打卡任务

    public void deletePunchClockByTaskId(Integer taskId); // 根据taskId删除对应的打卡记录

    public void deletePunchClockByUserId(Integer userId); // 删除用户下的所有打卡记录
}
