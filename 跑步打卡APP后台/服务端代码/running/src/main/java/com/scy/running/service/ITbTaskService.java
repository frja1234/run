package com.scy.running.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scy.running.model.TbPermission;
import com.scy.running.model.TbRole;
import com.scy.running.model.TbTask;
import com.scy.running.model.TbTaskVo;

import java.util.List;

/**
 * <p>
 * InnoDB free: 11264 kB 服务类
 * </p>
 *
 * @author scy
 * @since 2021-08-12
 */
public interface ITbTaskService extends IService<TbTask> {

    // 根据条件查询权限信息，自带分页功能
    public IPage<TbTask> selectTask(Page<TbTask> page, TbTask obj);
    // 新增任务
    public void insertTaskByObjAndClassIdList(TbTask obj, List<Integer> classIdList);

    public TbTask selectTaskById(Integer taskId); // 根据任务id查询任务的信息

    public void updateTaskByObj(TbTask obj); // 修改任务

    public void deleteTaskByObj(TbTask obj); // 删除任务

    public TbTaskVo selectProcessByTaskId(Integer taskId); // 根据任务id查询完成进度

    public void updateTaskByDate(String dateStr); // 根据日期修改任务超时
}
