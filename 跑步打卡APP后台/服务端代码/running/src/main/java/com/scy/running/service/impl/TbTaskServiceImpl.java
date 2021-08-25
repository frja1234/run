package com.scy.running.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scy.running.mapper.TbClassMapper;
import com.scy.running.mapper.TbPermissionMapper;
import com.scy.running.mapper.TbPunchClockMapper;
import com.scy.running.mapper.TbTaskMapper;
import com.scy.running.model.TbPermission;
import com.scy.running.model.TbPunchClock;
import com.scy.running.model.TbTask;
import com.scy.running.model.TbTaskVo;
import com.scy.running.service.ITbPermissionService;
import com.scy.running.service.ITbTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class TbTaskServiceImpl extends ServiceImpl<TbTaskMapper, TbTask> implements ITbTaskService {

    @Autowired
    private TbTaskMapper taskMapper;

    @Autowired
    private TbClassMapper classMapper;

    @Autowired
    private TbPunchClockMapper punchClockMapper;
    /**
     * 分页查询
     * @param page
     * @param obj
     * @return
     */
    @Override
    public IPage<TbTask> selectTask(Page<TbTask> page, TbTask obj) {
        return taskMapper.selectTask(page, obj);
    }

    /**
     * 新增任务
     * @param obj
     * @param classIdList
     */
    @Override
    public void insertTaskByObjAndClassIdList(TbTask obj, List<Integer> classIdList) {
        // 首先需要新增任务，然后通过mybatis返回主键
        taskMapper.insert(obj);

        // 然后就是通过classIdList, 到user表中查询有关的所有的userIdList
        List<Integer> userIdList = classMapper.selectUserIdByClassIdList(classIdList);

        Integer taskId = obj.getTaskId();
        if(taskId != null){
            // 组装List
            // List<TbPunchClock> punchClockList = new ArrayList<>();
            for(Integer userId : userIdList){
                TbPunchClock punchClock = new TbPunchClock();
                punchClock.setUserId(userId);
                punchClock.setTaskId(taskId);
                punchClock.setPunchClockState(0); // 是否已打卡
                punchClock.setRunTotalTime(0); // 已跑时间
                punchClock.setRunTotalLength(0); // 已跑路程
                // 最后就是通过返回的taskId主键以及userIdList到tb_punch_clock表中插入相应的数据
                punchClockMapper.insert(punchClock);
            }

        }
    }

    /**
     * 根据任务id查询任务的信息
     * @param taskId
     * @return
     */
    @Override
    public TbTask selectTaskById(Integer taskId) {
        return taskMapper.selectTaskById(taskId);
    }

    // 修改任务
    @Override
    public void updateTaskByObj(TbTask obj) {
        taskMapper.updateById(obj);
    }

    // 删除任务
    @Override
    public void deleteTaskByObj(TbTask obj) {
        // 先根据taskId删除对应的打卡记录
        punchClockMapper.deletePunchClockByTaskId(obj.getTaskId());
        // 然后再删除任务
        taskMapper.deleteById(obj.getTaskId());
    }

    // 根据任务id查询完成进度
    @Override
    public TbTaskVo selectProcessByTaskId(Integer taskId) {
        return taskMapper.selectProcessByTaskId(taskId);
    }

    // 根据日期修改任务超时
    @Override
    public void updateTaskByDate(String dateStr) {
       taskMapper.updateTaskByDate(dateStr);
    }

}
