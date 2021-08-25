package com.scy.running.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scy.running.model.TbPermission;
import com.scy.running.model.TbPunchClock;
import com.scy.running.model.TbTask;

import java.util.List;

/**
 * <p>
 * InnoDB free: 11264 kB 服务类
 * </p>
 *
 * @author scy
 * @since 2021-08-12
 */
public interface ITbPunchClockService extends IService<TbPunchClock> {

    public TbPunchClock selectPunchClockByUserIdApp(Integer userId); // 根据userId查找当天的打卡任务


    public void updatePunchClockByObj(TbPunchClock obj); // 修改打卡记录
}
