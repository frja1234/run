package com.scy.running.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scy.running.model.TbPermission;
import com.scy.running.model.TbTask;
import com.scy.running.model.TbTaskVo;
import io.swagger.models.auth.In;
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
public interface TbTaskMapper extends BaseMapper<TbTask> {
    /**
     * <p>
     * 查询 : 根据state状态查询用户列表，分页显示
     * </p>
     *
     * @param page 分页对象,xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
     * @param obj 查询条件对象
     * @return 分页对象
     */
    public IPage<TbTask> selectTask(Page<?> page, TbTask obj);

    public TbTask selectTaskById(Integer taskId); // 根据任务id查询任务的信息

    public TbTaskVo selectProcessByTaskId(Integer taskId); // 根据任务id查询完成进度

    public void updateTaskByDate(String dateStr); // 根据日期修改任务超时

}
