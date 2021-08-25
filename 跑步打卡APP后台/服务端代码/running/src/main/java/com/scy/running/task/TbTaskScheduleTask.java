package com.scy.running.task;

import com.scy.running.service.ITbTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TbTask表的定时任务类
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class TbTaskScheduleTask {

    @Autowired
    private ITbTaskService taskService;
    /**
     * 每5秒一次：0/5 * * * * ?
     * 每天23点执行一次：0 0 23 * * ?
     * 每天凌晨0点05分执行一次：0 5 0 * * ?
     * 每天凌晨1点执行一次：0 0 1 * * ?
     */
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)

    //3.添加定时任务
    @Scheduled(cron = "0 5 0 * * ?")
    private void configureTasks() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = simpleDateFormat.format(new Date());
        taskService.updateTaskByDate(dateStr);
    }
}
